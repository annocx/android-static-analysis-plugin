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

package com.camnter.android.staticanalysis.plugin.rules

/**
 * @author CaMnter
 */

class PmdRule {

    def ruleSet =
            """<?xml version="1.0"?>
<ruleset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" name="Android Application Rules"
    xmlns="http://pmd.sf.net/ruleset/1.0.0"
    xsi:noNamespaceSchemaLocation="http://pmd.sf.net/ruleset_xml_schema.xsd"
    xsi:schemaLocation="http://pmd.sf.net/ruleset/1.0.0 http://pmd.sf.net/ruleset_xml_schema.xsd">

    <description>Custom ruleset for Android application</description>

    <exclude-pattern>.*/R.java</exclude-pattern>
    <exclude-pattern>.*/gen/.*</exclude-pattern>

    <rule ref="rulesets/java/android.xml"/>
    <rule ref="rulesets/java/clone.xml"/>
    <rule ref="rulesets/java/finalizers.xml"/>
    <rule ref="rulesets/java/imports.xml">
        <!-- Espresso is designed this way !-->
        <exclude name="TooManyStaticImports"/>
    </rule>
    <rule ref="rulesets/java/logging-java.xml"/>
    <rule ref="rulesets/java/braces.xml"/>
    <rule ref="rulesets/java/strings.xml"/>
    <rule ref="rulesets/java/basic.xml"/>
    <rule ref="rulesets/java/naming.xml">
        <exclude name="AbstractNaming"/>
        <exclude name="LongVariable"/>
        <exclude name="ShortMethodName"/>
        <exclude name="ShortVariable"/>
        <exclude name="VariableNamingConventions"/>
    </rule>
</ruleset>
"""
}
