/*
 * Copyright (C) 2018 CaMnter yuanyu.camnter@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.camnter.android.staticanalysis.plugin.task.envelope

import com.camnter.android.staticanalysis.plugin.exception.ChainInterruptException
import com.camnter.android.staticanalysis.plugin.exception.MissingMailParameterException
import com.camnter.android.staticanalysis.plugin.extension.EmailExtension
import com.camnter.android.staticanalysis.plugin.utils.StringUtils
import com.sun.mail.util.MailSSLSocketFactory

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*

/**
 * @author CaMnter
 */

interface Envelope {

    static class HostDispatcher {

        static final def LOCAL_PROPERTIES = 'local.properties'
        static final def SMTP_HOST = 'asap.smtpHost'
        static final def SMTP_USER = 'asap.smtpUser'
        static final def SMTP_PASSWORD = 'asap.smtpPassword'

        static final String HOST_QQ = '@qq.com'
        static final String HOST_NETEASE = '@163.com'
        static final String HOST_GOOGLE = '@gmail.com'

        enum Host {
            QQ('QQ'), NetEase('NetEase'), Google('google'), Other('Other')

            String value

            Host(String value) {
                this.value = value
            }
        }

        static Host getHost(EnvelopeChainData data) {
            loadLocalProperties(data)
            String smtpUser = data.smtpUser
            if (smtpUser.endsWith(HOST_QQ)) {
                return Host.QQ
            } else if (smtpUser.endsWith(HOST_NETEASE)) {
                return Host.NetEase
            } else if (smtpUser.endsWith(HOST_GOOGLE)) {
                return Host.Google
            } else {
                return Host.Other
            }
        }

        static def loadLocalProperties(EnvelopeChainData data) {
            Properties localProperties = new Properties()
            localProperties.load(
                    data.project.rootProject.file(LOCAL_PROPERTIES).newDataInputStream())
            def smtpHost = localProperties.getProperty(SMTP_HOST)
            def smtpUser = localProperties.getProperty(SMTP_USER)
            def smtpPassword = localProperties.getProperty(SMTP_PASSWORD)
            if (StringUtils.isEmpty(smtpHost)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                        SMTP_HOST)
            }
            if (StringUtils.isEmpty(smtpUser)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                        SMTP_USER)
            }
            if (StringUtils.isEmpty(smtpPassword)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.LOCAL,
                        SMTP_PASSWORD)
            }
            data.smtpHost = smtpHost
            data.smtpUser = smtpUser
            data.smtpPassword = smtpPassword
        }
    }

    /**
     * base action
     * */
    interface EnvelopeChain {

        void duty()
    }

    /**
     * base chain
     *
     * @param < C >
     */
    static abstract class BaseEnvelopeChain<C extends BaseEnvelopeChain>
            implements EnvelopeChain {

        C next
        EnvelopeChainData input

        BaseEnvelopeChain(EnvelopeChainData input) {
            this.input = input
        }

        void execute() {
            try {
                duty()
                if (next != null) next.execute()
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    /**
     * receiver check chain
     *
     * @param < C >
     */
    static class ReceiversCheckChain<C extends BaseEnvelopeChain>
            extends BaseEnvelopeChain<C> {

        ReceiversCheckChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        void duty() {
            if (StringUtils.isEmpty(input.email.receivers)) {
                throw MissingMailParameterException(MissingMailParameterException.Where.EXTENSION,
                        'receivers')
            }
        }
    }

    /**
     * zip check chain
     *
     * @param < C >
     */
    static class ZipCheckChain<C extends BaseEnvelopeChain>
            extends BaseEnvelopeChain<C> {

        ZipCheckChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        void duty() {
            // zip check
            File zipFile = new File(input.zipPath)
            if (!zipFile.exists()) {
                throw ChainInterruptException("${zipFile.absolutePath} was not found",
                        ZipCheckChain.class)
            }
            input.zipFile = zipFile
        }
    }

    /**
     * html check chain
     *
     * @param < C >
     */
    static class HtmlCheckChain<C extends BaseEnvelopeChain>
            extends BaseEnvelopeChain<C> {

        HtmlCheckChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        void duty() {
            // html check
            List<File> safeHtmlFiles = new ArrayList<>()
            for (String path : input.htmlPaths) {
                File htmlFile = new File(path)
                if (!htmlFile.exists()) {
                    printf "%-29s = %s\n",
                            ['[EmailTask]   [sendHtmlEmail]', "${htmlFile.absolutePath} was not found"]
                } else {
                    safeHtmlFiles.add(htmlFile)
                }
            }
            if (safeHtmlFiles.size() == 0) {
                throw ChainInterruptException("no html files",
                        HtmlCheckChain.class)
            }
            input.safeHtmlFiles = safeHtmlFiles
        }
    }

    static abstract class SessionChain<C extends BaseEnvelopeChain>
            extends BaseEnvelopeChain<C> {

        static final def JAVA_MAIL_SMTP_HOST = 'mail.smtp.host'
        static final def JAVA_MAIL_SMTP_AUTH = 'mail.smtp.auth'
        static final def JAVA_MAIL_SMTP_SSL_ENABLE = 'mail.smtp.ssl.enable'
        static final def JAVA_MAIL_SMTP_SSL_SOCKET_FACTORY = 'mail.smtp.ssl.socketFactory'

        SessionChain(EnvelopeChainData input) {
            super(input)
        }

        abstract Authenticator getAuthenticator()

        @Override
        void duty() {
            Authenticator authenticator = getAuthenticator()
            Properties properties = this.getProperties()
            if (EmailExtension.ZIP == input.email.enclosureType) {
                if (authenticator == null) {
                    input.session = Session.getInstance(properties)
                } else {
                    input.session = Session.getInstance(properties, authenticator)
                }
            } else if (EmailExtension.HTML == input.email.enclosureType) {
                def sessionCount = input.safeHtmlFiles.size()
                input.sessions = new ArrayList<>(sessionCount)
                for (int i = 0; i < sessionCount; i++) {
                    if (authenticator == null) {
                        input.sessions.add(Session.getInstance(properties))
                    } else {
                        input.sessions.add(Session.getInstance(properties, authenticator))
                    }
                }
            }
        }

        Properties getProperties() {
            Properties properties = System.getProperties()
            properties.setProperty(JAVA_MAIL_SMTP_HOST, input.smtpHost)
            properties.setProperty(JAVA_MAIL_SMTP_AUTH, "true")
            return properties
        }
    }

    /**
     * default session chain
     *
     * @param < C >
     */
    static class DefaultSessionChain<C extends BaseEnvelopeChain>
            extends SessionChain<C> {

        DefaultSessionChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        Authenticator getAuthenticator() {
            return null
        }
    }

    /**
     * NetEase session chain
     *
     * @param < C >
     */
    static class NetEaseSessionChain<C extends BaseEnvelopeChain>
            extends SessionChain<C> {

        NetEaseSessionChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        Authenticator getAuthenticator() {
            // NetEase smtp
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(input.smtpUser, input.smtpPassword)
                }
            }
        }
    }

    /**
     * QQ session chain
     *
     * @param < C >
     */
    static class QQSessionChain<C extends BaseEnvelopeChain>
            extends DefaultSessionChain<C> {

        QQSessionChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        Authenticator getAuthenticator() {
            // QQ smtp
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(input.smtpUser, input.smtpPassword)
                }
            }
        }

        @Override
        Properties getProperties() {
            Properties properties = super.getProperties()
            MailSSLSocketFactory sslSocketFactory = new MailSSLSocketFactory()
            sslSocketFactory.setTrustAllHosts(true)
            properties.put(JAVA_MAIL_SMTP_SSL_ENABLE, "true")
            properties.put(JAVA_MAIL_SMTP_SSL_SOCKET_FACTORY, sslSocketFactory)
            return super.getProperties()
        }
    }

    /**
     * Google session chain
     *
     * @param < C >
     */
    static class GoogleSessionChain<C extends BaseEnvelopeChain>
            extends QQSessionChain<C> {

        GoogleSessionChain(EnvelopeChainData input) {
            super(input)
        }
    }

    /**
     * zip letter session chain
     *
     * @param < C >
     */
    static class ZipLetterChain<C extends BaseEnvelopeChain>
            extends BaseEnvelopeChain<C> {

        static final def DIVIDE = ';'

        ZipLetterChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        void duty() {
            EmailExtension email = input.email
            Session session = input.session
            String smtpUser = input.smtpUser
            String smtpPassword = input.smtpPassword
            File zipFile = input.zipFile

            MimeMessage message = new MimeMessage(session)

            // from & nickname
            if (StringUtils.isEmpty(email.nickname)) {
                message.setFrom(new InternetAddress(smtpUser))
            } else {
                InternetAddress from = new InternetAddress(MimeUtility.encodeWord(
                        MimeUtility.encodeWord("${email.nickname}") + " <${smtpUser}>"))
                message.setFrom(from)
            }

            // to
            if (!StringUtils.isEmpty(email.receivers)) {
                if (email.receivers.contains(DIVIDE)) {
                    String neatReceivers = StringUtils.replaceBlank(email.receivers)
                    String[] receivers = neatReceivers.split(DIVIDE)
                    InternetAddress[] addresses = new InternetAddress[receivers.length]
                    for (int i = 0; i < receivers.length; i++) {
                        addresses[i] = new InternetAddress(receivers[i])
                    }
                    message.addRecipients(Message.RecipientType.TO, addresses)
                } else {
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(email.receivers))
                }
            }

            // cc
            if (!StringUtils.isEmpty(email.carbonCopy)) {
                if (email.carbonCopy.contains(DIVIDE)) {
                    String neatCarbonCopys = StringUtils.replaceBlank(email.carbonCopy)
                    String[] carbonCopys = neatCarbonCopys.split(DIVIDE)
                    InternetAddress[] addresses = new InternetAddress[carbonCopys.length]
                    for (int i = 0; i < carbonCopys.length; i++) {
                        addresses[i] = new InternetAddress(carbonCopys[i])
                    }
                    message.addRecipients(Message.RecipientType.CC, addresses)
                } else {
                    message.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(email.carbonCopy))
                }
            }

            // theme
            message.setSubject(email.theme)

            // content
            BodyPart contentbodyPart = new MimeBodyPart()
            if (!StringUtils.isEmpty(email.content)) {
                contentbodyPart.setText(email.content + '\n\n\n\n')
            }


            // enclosure
            BodyPart enclosureBodyPart = new MimeBodyPart()
            DataSource source = new FileDataSource(zipFile.absolutePath)
            enclosureBodyPart.setDataHandler(new DataHandler(source))
            enclosureBodyPart.setFileName(zipFile.name)

            // multipart
            Multipart multipart = new MimeMultipart()
            multipart.addBodyPart(contentbodyPart)
            multipart.addBodyPart(enclosureBodyPart)

            message.setContent(multipart)
            Transport.send(message, smtpUser, smtpPassword)
            printf "%-32s = %s\n",
                    ['[EmailTask]   [sendEnclosureZip]', "${zipFile.absolutePath}"]
        }
    }

    /**
     * html letter session chain
     *
     * @param < C >
     */
    static class HtmlLetterChain<C extends BaseEnvelopeChain>
            extends BaseEnvelopeChain<C> {

        static final def DIVIDE = ';'

        HtmlLetterChain(EnvelopeChainData input) {
            super(input)
        }

        @Override
        void duty() {
            EmailExtension email = input.email
            List<Session> sessions = input.sessions
            String smtpUser = input.smtpUser
            String smtpPassword = input.smtpPassword
            List<File> safeHtmlFiles = input.safeHtmlFiles

            for (int i = 0; i < safeHtmlFiles.size(); i++) {
                File htmlFile = safeHtmlFiles.get(i)
                if (!htmlFile.exists()) continue
                Session session = sessions.get(i)

                MimeMessage message = new MimeMessage(session)

                // from & nickname
                if (StringUtils.isEmpty(email.nickname)) {
                    message.setFrom(new InternetAddress(smtpUser))
                } else {
                    InternetAddress from = new InternetAddress(MimeUtility.encodeWord(
                            MimeUtility.encodeWord("${email.nickname}") + " <${smtpUser}>"))
                    message.setFrom(from)
                }

                // to
                if (!StringUtils.isEmpty(email.receivers)) {
                    if (email.receivers.contains(DIVIDE)) {
                        String neatReceivers = StringUtils.replaceBlank(email.receivers)
                        String[] receivers = neatReceivers.split(DIVIDE)
                        InternetAddress[] addresses = new InternetAddress[receivers.length]
                        for (int j = 0; j < receivers.length; j++) {
                            addresses[j] = new InternetAddress(receivers[j])
                        }
                        message.addRecipients(Message.RecipientType.TO, addresses)
                    } else {
                        message.addRecipient(Message.RecipientType.TO,
                                new InternetAddress(email.receivers))
                    }
                }

                // cc
                if (!StringUtils.isEmpty(email.carbonCopy)) {
                    if (email.carbonCopy.contains(DIVIDE)) {
                        String neatCarbonCopys = StringUtils.replaceBlank(email.carbonCopy)
                        String[] carbonCopys = neatCarbonCopys.split(DIVIDE)
                        InternetAddress[] addresses = new InternetAddress[carbonCopys.length]
                        for (int j = 0; j < carbonCopys.length; j++) {
                            addresses[j] = new InternetAddress(carbonCopys[j])
                        }
                        message.addRecipients(Message.RecipientType.CC, addresses)
                    } else {
                        message.addRecipient(Message.RecipientType.CC,
                                new InternetAddress(email.carbonCopy))
                    }
                }

                // theme
                message.setSubject(email.theme)

                StringBuilder builder = new StringBuilder()
                htmlFile.eachLine { String line -> builder.append(line) }

                BodyPart contentBodyPart = new MimeBodyPart()
                if (!StringUtils.isEmpty(email.content)) {
                    contentBodyPart.setText(builder.toString(), "utf-8", "html")
                }

                BodyPart dividerPart = new MimeBodyPart()
                dividerPart.setText("\n\n\n")

                BodyPart enclosureBodyPart = new MimeBodyPart()
                DataSource source = new FileDataSource(htmlFile.absolutePath)
                enclosureBodyPart.setDataHandler(new DataHandler(source))
                enclosureBodyPart.setFileName(htmlFile.name)

                // content
                Multipart multipart = new MimeMultipart()
                multipart.addBodyPart(contentBodyPart)
                multipart.addBodyPart(dividerPart)
                multipart.addBodyPart(enclosureBodyPart)

                message.setContent(multipart)
                Transport.send(message, smtpUser, smtpPassword)
                printf "%-29s = %s\n",
                        ['[EmailTask]   [sendHtmlEmail]', "${htmlFile.absolutePath}"]
            }
        }
    }
}
