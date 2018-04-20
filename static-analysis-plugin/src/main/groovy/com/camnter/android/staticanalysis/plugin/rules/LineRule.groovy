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

class LineRule {

    def config =
            """<?xml version="1.0" encoding="UTF-8"?>
<lint>
    <issue id="AdapterViewChildren" severity="ignore"/>
    <issue id="AllowBackup" severity="ignore"/>
    <issue id="AlwaysShowAction" severity="ignore"/>
    <issue id="ButtonCase" severity="ignore"/>
    <issue id="ButtonOrder" severity="ignore"/>
    <issue id="ButtonStyle" severity="ignore"/>
    <issue id="CommitPrefEdits" severity="ignore"/>
    <issue id="CommitTransaction" severity="ignore"/>
    <issue id="ContentDescription" severity="ignore"/>
    <issue id="CutPasteId" severity="ignore"/>
    <issue id="DalvikOverride" severity="ignore"/>
    <issue id="DefaultLocale" severity="ignore"/>
    <issue id="Deprecated" severity="ignore"/>
    <issue id="DeviceAdmin" severity="ignore"/>
    <issue id="DisableBaselineAlignment" severity="ignore"/>
    <issue id="DrawAllocation" severity="ignore"/>
    <issue id="DuplicateActivity" severity="ignore"/>
    <issue id="DuplicateDefinition" severity="ignore"/>
    <issue id="DuplicateIds" severity="ignore"/>
    <issue id="DuplicateIncludedIds" severity="ignore"/>
    <issue id="DuplicateUsesFeature" severity="ignore"/>
    <issue id="EnforceUTF8" severity="ignore"/>
    <issue id="ExportedContentProvider" severity="ignore"/>
    <issue id="ExportedReceiver" severity="ignore"/>
    <issue id="ExportedService" severity="ignore"/>
    <issue id="ExtraText" severity="ignore"/>
    <issue id="ExtraTranslation" severity="ignore"/>
    <issue id="FloatMath" severity="ignore"/>
    <issue id="GifUsage" severity="ignore"/>
    <issue id="GradleOverrides" severity="ignore"/>
    <issue id="GrantAllUris" severity="ignore"/>
    <issue id="GridLayout" severity="ignore"/>
    <issue id="HandlerLeak" severity="ignore"/>
    <issue id="HardcodedDebugMode" severity="ignore"/>
    <issue id="HardcodedText" severity="ignore"/>
    <issue id="IconColors" severity="ignore"/>
    <issue id="IconDensities" severity="error"/>
    <issue id="IconDipSize" severity="error"/>
    <issue id="IconDuplicates" severity="ignore"/>
    <issue id="IconDuplicatesConfig" severity="ignore"/>
    <issue id="IconExtension" severity="ignore"/>
    <issue id="IconLauncherShape" severity="ignore"/>
    <issue id="IconLocation" severity="ignore"/>
    <issue id="IconMissingDensityFolder" severity="ignore"/>
    <issue id="IconMixedNinePatch" severity="ignore"/>
    <issue id="IconNoDpi" severity="ignore"/>
    <issue id="IconXmlAndPng" severity="ignore"/>
    <issue id="IllegalResourceRef" severity="ignore"/>
    <issue id="InOrMmUsage" severity="ignore"/>
    <issue id="InconsistentArrays" severity="ignore"/>
    <issue id="InconsistentLayout" severity="ignore"/>
    <issue id="InefficientWeight" severity="ignore"/>
    <issue id="InlinedApi" severity="ignore"/>
    <issue id="InnerclassSeparator" severity="ignore"/>
    <issue id="Instantiatable" severity="ignore"/>
    <issue id="InvalidId" severity="ignore"/>
    <issue id="InvalidPackage" severity="ignore"/>
    <issue id="JavascriptInterface" severity="ignore"/>
    <issue id="LabelFor" severity="ignore"/>
    <issue id="LibraryCustomView" severity="ignore"/>
    <issue id="LocalSuppress" severity="ignore"/>
    <issue id="MangledCRLF" severity="ignore"/>
    <issue id="ManifestOrder" severity="ignore"/>
    <issue id="ManifestTypo" severity="ignore"/>
    <issue id="MenuTitle" severity="ignore"/>
    <issue id="MergeRootFrame" severity="ignore"/>
    <issue id="MissingApplicationIcon" severity="ignore"/>
    <issue id="MissingId" severity="ignore"/>
    <issue id="MissingPrefix" severity="ignore"/>
    <issue id="MissingQuantity" severity="ignore"/>
    <issue id="MissingRegistered" severity="ignore"/>
    <issue id="MissingSuperCall" severity="ignore"/>
    <issue id="MissingTranslation" severity="ignore"/>
    <issue id="MissingVersion" severity="ignore"/>
    <issue id="MockLocation" severity="ignore"/>
    <issue id="MultipleUsesSdk" severity="ignore"/>
    <issue id="NamespaceTypo" severity="ignore"/>
    <issue id="NestedScrolling" severity="ignore"/>
    <issue id="NestedWeights" severity="ignore"/>
    <issue id="NewApi" severity="ignore"/>
    <issue id="NotSibling" severity="ignore"/>
    <issue id="ObsoleteLayoutParam" severity="ignore"/>
    <issue id="OldTargetApi" severity="ignore"/>
    <issue id="OnClick" severity="ignore"/>
    <issue id="Orientation" severity="ignore"/>
    <issue id="Overdraw" severity="ignore"/>
    <issue id="Override" severity="ignore"/>
    <issue id="PackagedPrivateKey" severity="ignore"/>
    <issue id="ParcelCreator" severity="ignore"/>
    <issue id="PrivateResource" severity="ignore"/>
    <issue id="Proguard" severity="ignore"/>
    <issue id="ProguardSplit" severity="ignore"/>
    <issue id="ProtectedPermissions" severity="ignore"/>
    <issue id="PxUsage" severity="ignore"/>
    <issue id="Recycle" severity="ignore"/>
    <issue id="Registered" severity="ignore"/>
    <issue id="RequiredSize" severity="ignore"/>
    <issue id="ResAuto" severity="ignore"/>
    <issue id="ResourceAsColor" severity="ignore"/>
    <issue id="ScrollViewCount" severity="ignore"/>
    <issue id="ScrollViewSize" severity="ignore"/>
    <issue id="SdCardPath" severity="ignore"/>
    <issue id="SecureRandom" severity="ignore"/>
    <issue id="ServiceCast" severity="ignore"/>
    <issue id="SetJavaScriptEnabled" severity="ignore"/>
    <issue id="ShowToast" severity="ignore"/>
    <issue id="SimpleDateFormat" severity="ignore"/>
    <issue id="SmallSp" severity="ignore"/>
    <issue id="SpUsage" severity="ignore"/>
    <issue id="StateListReachable" severity="ignore"/>
    <issue id="StringFormatCount" severity="ignore"/>
    <issue id="StringFormatInvalid" severity="ignore"/>
    <issue id="StringFormatMatches" severity="ignore"/>
    <issue id="StyleCycle" severity="ignore"/>
    <issue id="Suspicious0dp" severity="ignore"/>
    <issue id="SuspiciousImport" severity="ignore"/>
    <issue id="TextFields" severity="ignore"/>
    <issue id="TextViewEdits" severity="ignore"/>
    <issue id="TooDeepLayout" severity="ignore"/>
    <issue id="TooManyViews" severity="ignore"/>
    <issue id="TrulyRandom" severity="ignore"/>
    <issue id="TypographyDashes" severity="ignore"/>
    <issue id="TypographyEllipsis" severity="ignore"/>
    <issue id="TypographyFractions" severity="ignore"/>
    <issue id="TypographyOther" severity="ignore"/>
    <issue id="Typos" severity="ignore"/>
    <issue id="UniquePermission" severity="ignore"/>
    <issue id="UnknownId" severity="ignore"/>
    <issue id="UnknownIdInLayout" severity="ignore"/>
    <issue id="UnlocalizedSms" severity="ignore"/>
    <issue id="UnusedNamespace" severity="ignore"/>
    <issue id="UnusedQuantity" severity="ignore"/>
    <issue id="UnusedResources" severity="ignore"/>
    <issue id="UseCheckPermission" severity="ignore"/>
    <issue id="UseCompoundDrawables" severity="ignore"/>
    <issue id="UseSparseArrays" severity="ignore"/>
    <issue id="UseValueOf" severity="ignore"/>
    <issue id="UselessLeaf" severity="ignore"/>
    <issue id="UselessParent" severity="ignore"/>
    <issue id="UsesMinSdkAttributes" severity="ignore"/>
    <issue id="ValidFragment" severity="ignore"/>
    <issue id="ViewConstructor" severity="ignore"/>
    <issue id="ViewTag" severity="ignore"/>
    <issue id="Wakelock" severity="ignore"/>
    <issue id="WorldReadableFiles" severity="ignore"/>
    <issue id="WorldWriteableFiles" severity="ignore"/>
    <issue id="WrongCall" severity="ignore"/>
    <issue id="WrongCase" severity="ignore"/>
    <issue id="WrongFolder" severity="ignore"/>
    <issue id="WrongManifestParent" severity="ignore"/>
    <issue id="WrongViewCast" severity="ignore"/>
</lint>
"""
}
