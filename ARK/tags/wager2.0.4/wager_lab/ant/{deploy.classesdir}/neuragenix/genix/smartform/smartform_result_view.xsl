<?xml version="1.0" encoding="utf-8"?>

<!-- 
    smartform_list.xsl, part of the Smartform channel
    Author: hhoang@neuragenix.com
    Date: 01/04/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../../common/common_btn_name.xsl"/>
<xsl:include href="./header.xsl"/>
    
    <xsl:param name="formParams">current=smartform_result_view</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="downloadURL">downloadURL_false</xsl:param>   
    <xsl:param name="patientChannelURL">patientChannelURL_false</xsl:param>
    <xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
    <xsl:param name="BiospecimenChannelURL">BiospecimenChannelURL_false</xsl:param>
    <xsl:param name="BiospecimenChannelTabOrder">BiospecimenChannelTabOrder</xsl:param>
    
    
    <xsl:param name="nodeId">nodeId_false</xsl:param>
  
    <xsl:template match="smartform">
    
    <xsl:param name="fromPatient"><xsl:value-of select="fromPatient" /></xsl:param>
    <xsl:param name="fromPatientAnalysis"><xsl:value-of select="fromPatientAnalysis" /></xsl:param>
    <xsl:param name="PATIENT_intPatientID"><xsl:value-of select="PATIENT_intPatientID" /></xsl:param>    
    <xsl:param name="fromSFBatchGeneration"><xsl:value-of select="fromSFBatchGeneration" /></xsl:param>
    <xsl:param name="fromBiospecimenKey"><xsl:value-of select="fromBiospecimenKey"/></xsl:param>    
    
    <xsl:param name="domain"><xsl:value-of select="domain" /></xsl:param>
    <xsl:param name="participant"><xsl:value-of select="participant" /></xsl:param>
    <xsl:param name="strTitle"><xsl:value-of select="strTitle" /></xsl:param>
    <xsl:param name="strBackButton"><xsl:value-of select="strBackButton" /></xsl:param>
    <xsl:param name="pagecount"><xsl:value-of select="pagecount" /></xsl:param>
    <xsl:param name="currentpage"><xsl:value-of select="currentpage" /></xsl:param>
    <xsl:param name="startorder"><xsl:value-of select="startorder" /></xsl:param>
    <xsl:param name="endorder"><xsl:value-of select="endorder" /></xsl:param>
    <xsl:param name="SMARTFORM_smartformname"><xsl:value-of select="SMARTFORM_smartformname" /></xsl:param>
    <xsl:param name="SMARTFORMPARTICIPANTS_intSmartformID"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" /></xsl:param>
    <xsl:param name="SMARTFORMPARTICIPANTS_intSmartformParticipantID"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformParticipantID" /></xsl:param>
    <xsl:param name="SMARTFORMPARTICIPANTS_strSmartformStatus"><xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus" /></xsl:param>
    <xsl:param name="SMARTFORMPARTICIPANTS_intCurrentPage"><xsl:value-of select="SMARTFORMPARTICIPANTS_intCurrentPage" /></xsl:param>
    <xsl:param name="intStudyID"><xsl:value-of select="intStudyID" /></xsl:param>
    <xsl:param name="lookupReload"><xsl:value-of select="lookupReload" /></xsl:param>
    <xsl:param name="var1"><xsl:value-of select="var1" /></xsl:param>
    <xsl:param name="var2"><xsl:value-of select="var2" /></xsl:param>
    <xsl:param name="var3"><xsl:value-of select="var3" /></xsl:param>
    <xsl:param name="var4"><xsl:value-of select="var4" /></xsl:param>
    <xsl:param name="PATIENT_strSurname"><xsl:value-of select="PATIENT_strSurname" /></xsl:param>
    <xsl:param name="PATIENT_strFirstName"><xsl:value-of select="PATIENT_strFirstName" /></xsl:param>
    
    
	<script language="javascript"> 
	
	function checkboxs() 
	{		
		for (var ax=0; ax &lt; document.forms[0].elements.length; ax++) 
		{
			var elem = document.forms[0].elements[ax];
			if (elem.type == 'checkbox') 
			{
				elem.checked='checked'
		    }
		}
		
	}
	function changeValue(a)
	{
		
		if (a.value == 'yes')
		{
			a.value='no'
		}
		else
		{
			a.value='yes'
		}	
		//window.alert (a.value)
	}


	function confirmDelete(tfBox, aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		getTextValue(tfBox, aURL);
            }
	}
        
        function jumpTo(aURL){
            window.location=aURL;
        }
        
        function getTextValue(tfBox, aURL){
            
            aURL = aURL + '&amp;SMARTFORMPARTICIPANTS_strUserNote=' + tfBox.value;
            jumpTo(aURL);
        }

        function mySubmit(aURL){
        
            document.myForm.lookupReload.value = true;
            document.myForm.submit();         
        }
        
        function jumpToDiagnosis(aURL) {
            document.myForm.action = aURL;
            document.myForm.submit();
        }
        
        function addRepeatableDE(DEOrder){
            document.myForm.AddRepeatableDE.value = DEOrder;
            document.myForm.submit();         
        }
        
        function removeRepeatableDE(DEOrder, index){        
            document.myForm.RemoveRepeatableDE.value = DEOrder;
            document.myForm.RemoveRepeatableDEIndex.value = index;
            document.myForm.submit();         
        }                
    </script>
    
    <form name="myForm" action="{$baseActionURL}?{$formParams}&amp;uP_root=root" method="post" enctype="multipart/form-data" onSubmit="javascript:checkboxs();">
    
    <xsl:choose>
    <xsl:when test="(string-length( $fromSFBatchGeneration ) > 0)">
        <xsl:call-template name="header">
                <xsl:with-param name="activeSubtab">smartform</xsl:with-param>
                <xsl:with-param name="previousButtonFlag">true</xsl:with-param>
		<xsl:with-param name="previousButtonUrl"><xsl:value-of select="$BiospecimenChannelURL"/>?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=<xsl:value-of select="$BiospecimenChannelTabOrder"/>&amp;module=BATCH_SFRESULTS_GENERATION&amp;action=specimen_selection</xsl:with-param>
                <xsl:with-param name="nextButtonFlag">true</xsl:with-param>
		<xsl:with-param name="nextButtonUrl"><xsl:value-of select="$BiospecimenChannelURL"/>?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=<xsl:value-of select="$BiospecimenChannelTabOrder"/>&amp;module=BATCH_SFRESULTS_GENERATION&amp;action=GenerateSFResults</xsl:with-param>                
        </xsl:call-template>    
    </xsl:when>
    <xsl:otherwise>
        <table width="100%">
            <tr>
                <td width="10%"></td>
                <td width="2px" bgcolor="black"></td>
                <td width="1%"></td>
                <td width="24%" class="uportal-label">
                   <xsl:if test="$domain = 'Study'">
                       Smartform: <xsl:value-of select="SMARTFORM_smartformname"/>
                   </xsl:if> 
                   <xsl:if test="$domain = 'Biospecimen'">
                       Smartform: <xsl:value-of select="SMARTFORM_smartformname"/>
                   </xsl:if> 
                   <xsl:if test="$domain = 'Bioanalysis'">
                       Smartform: <xsl:value-of select="SMARTFORM_smartformname"/> <br/>
                       &#160; Created by: <xsl:value-of select="var3" /> <br/>
                       &#160; Date Created: <xsl:value-of select="var4" />                       
                   </xsl:if>                                       
               </td>
                <td width="2px" bgcolor="black"></td>
                <td width="1%"></td>
                <xsl:if test="$domain = 'Biospecimen'">
                <td width="20%" class="uportal-label">
                    <xsl:if test="string-length(PATIENT_strSurname) > 0"> 
                        <xsl:value-of select="PATIENT_strSurname" />, <xsl:value-of select="PATIENT_strFirstName" /> <br/> 
                    </xsl:if>
                    
                    Biospecimen : <xsl:value-of select="strTitle" /><!--xsl:value-of select="participant" /-->
                </td>
                <td width="20%" class="uportal-label">
                    <xsl:if test="string-length(var1) > 0 and $var1 != null">
                    Type : <xsl:value-of select="var1" />
                    </xsl:if> 
                </td>
                <td width="19%" class="uportal-label">
                    <xsl:if test="string-length(var2) > 0 and $var2 != null">
                        Date : <xsl:value-of select="var2" />
                    </xsl:if>    
                </td>
                </xsl:if>

                <xsl:if test="$domain = 'Bioanalysis'">
                <td width="20%" class="uportal-label">
                    <xsl:if test="string-length(PATIENT_strSurname) > 0"> 
                        <xsl:value-of select="PATIENT_strSurname" />, <xsl:value-of select="PATIENT_strFirstName" /> <br/> 
                    </xsl:if>
                     Biospecimen : <xsl:value-of select="strTitle" /><!--xsl:value-of select="participant" /-->
                </td>
                <td width="20%" class="uportal-label">
                    <xsl:if test="string-length(var1) > 0 and $var1 != null">
                        Type : <xsl:value-of select="var1" /> 
                     </xsl:if>
                </td>
                <td width="19%" class="uportal-label">
                    <xsl:if test="string-length(var2) > 0 and $var2 != null">
                        Date : <xsl:value-of select="var2" />
                    </xsl:if>    
                </td>
                </xsl:if>

                <xsl:if test="$domain = 'Admissions'">
                <td width="20%" class="uportal-label">
                    Collection Group : <xsl:value-of select="strTitle" /><!--xsl:value-of select="participant" /-->
                </td>
                <td width="20%" class="uportal-label">
                    Patient ID:<xsl:value-of select="var1" /> 
                </td>
                <td width="19%" class="uportal-label">
                    Patient Name:<xsl:value-of select="var2" />
                </td>
                </xsl:if>              

                <xsl:if test="$domain = 'Study'">
                <td width="15%" class="uportal-label">
                    Patient : <xsl:value-of select="strTitle" />    
                </td>
                <td width="22%" class="uportal-label">
                Name : <xsl:value-of select="var1" /> 
                </td>
                <td width="22%" class="uportal-label">
                Surname : <xsl:value-of select="var2" />
                </td>
                </xsl:if>
                <xsl:if test="$domain = 'CASE'">
                <td width="20%" class="uportal-label">
                    Activity details
                </td>
                <td width="39%" class="uportal-label">
                    Ref: <xsl:value-of select="strTitle" /><!--xsl:value-of select="participant" /-->
                </td>
                </xsl:if>
                <td width="5%">
                    <xsl:choose>
                    <xsl:when test="$domain = 'CASE'">
                        <input type="submit" name="back" tabindex="1" value="{$backBtnLabel}" class="uportal-button" />
                    </xsl:when>
                    <xsl:when test="($domain = 'Study') and (string-length( $fromPatient ) = 0) ">
                        <input type="submit" name="back" tabindex="1" value="{$backBtnLabel}" class="uportal-button" />
                    </xsl:when>
                    <xsl:when test="$domain = 'Biospecimen'">
                        <input type="submit" name="back" tabindex="1" value="{$backBtnLabel}" class="uportal-button" />
                    </xsl:when>
                    <xsl:when test="$domain = 'Bioanalysis'">
                    <xsl:choose>
                    <xsl:when test="(substring(fromPatientAnalysis,'true'))">
                        <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}?uP_root=root&amp;strcurrent=patient_view&amp;domain=Bioanalysis&amp;PATIENT_intPatientID={$PATIENT_intPatientID}')" />
                    </xsl:when> 
                    <xsl:otherwise>
                        <input type="submit" name="back" tabindex="1" value="{$backBtnLabel}" class="uportal-button" />
                    </xsl:otherwise>
                    </xsl:choose>                                                               
                    </xsl:when>
                    <xsl:when test="$domain = 'Admissions'">
                        <input type="submit" name="back" tabindex="1" value="{$backBtnLabel}" class="uportal-button" />
                    </xsl:when>
                    <xsl:when test="(string-length( $fromPatient ) > 0)">
                        <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$patientChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;PATIENT_intInternalPatientID={$participant}')" />
                    </xsl:when>

                    </xsl:choose>
                </td>
            </tr>            

        </table> 
        
        <hr/>   
    </xsl:otherwise>
    </xsl:choose>

    <table width="100%">
        <tr>
            <td width="10%"></td>
            <td width="60%" class="neuragenix-form-required-text"><xsl:value-of select="strErrorMessage" /><xsl:value-of select="strErrorCompleted" /></td>
            <td width="30%" class="neuragenix-form-required-text"></td>
        </tr>
    </table>
    
    <xsl:for-each select="search_dataelement">
        <xsl:variable name="varDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:variable>
        <xsl:variable name="varDataElementHelp"><xsl:value-of select="DATAELEMENTS_strDataElementHelp" /></xsl:variable>
        <xsl:variable name="varDataElementMandatory"><xsl:value-of select="DATAELEMENTS_intMandatory" /></xsl:variable>
        <xsl:variable name="varDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>
        
        
        <xsl:choose>
            <!-- TITLE data element -->
            <xsl:when test="$varDataElementType = 'TITLE'">
                <table width="100%">
                    <tr>
                        <td width="10%"></td>
                        <td width="90%" class="uportal-channel-table-header">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                        </td>
                    </tr>
                </table>
            </xsl:when>
            
            <!-- COMMENT data element -->
            <xsl:when test="$varDataElementType = 'COMMENT'">
                <table width="100%">
                    <tr>
                        <td width="10%"></td>
                        <td width="90%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                        </td>
                    </tr>
                </table>
            </xsl:when>
            
            <!-- TEXT data element -->
            <xsl:when test="$varDataElementType = 'TEXT'">
                <xsl:variable name="varRow"><xsl:value-of select="DATAELEMENTS_intDataElementRow" /></xsl:variable>
                <xsl:variable name="varColumn"><xsl:value-of select="DATAELEMENTS_intDataElementColumn" /></xsl:variable>
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <textarea name="{$varOrder}" rows="{$varRow}" cols="{$varColumn}" class="uportal-input-text">
                                <xsl:value-of select="value" />
                            </textarea>
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            <!--CHECKBOX data element-->
			
            <xsl:when test="$varDataElementType = 'CHECK BOX'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varValue"><xsl:value-of select="value" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <input onClick="javascript:changeValue(this);" type="checkbox" name="{$varOrder}" class="uportal-input-text" style="text-align: left" >
								<xsl:choose>
								<xsl:when test="$varValue = 'yes'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
									<xsl:attribute name="value" >yes</xsl:attribute>
								</xsl:when>	
								 <xsl:otherwise>
									<xsl:attribute name="value" >no</xsl:attribute>
								 </xsl:otherwise>
								</xsl:choose>																							
							</input>
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            <!-- NUMERIC data element -->
            <xsl:when test="$varDataElementType = 'NUMERIC'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varValue"><xsl:value-of select="value" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <input type="text" name="{$varOrder}" value="{$varValue}" size="20" class="uportal-input-text" style="text-align: left" />
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            
            <!-- MONETARY data element -->
            <xsl:when test="$varDataElementType = 'MONETARY'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varValue"><xsl:value-of select="value" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        
                        <td width="1%" class="uportal-label">&#36;</td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <input type="text" name="{$varOrder}" value="{$varValue}" size="20" class="uportal-input-text" style="text-align: left" />
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            
            <!-- DROPDOWN data element -->
            <xsl:when test="$varDataElementType = 'DROPDOWN'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <select name="{$varOrder}" class="uportal-input-text">
                                <option value=""/>                    
                               
                                <xsl:for-each select="option">                                    
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="optionvalue" /></xsl:attribute>
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="optionlabel" />
                                    </option>
                                </xsl:for-each>
                            </select>
                           
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            
            <!-- DATE data element -->
            <xsl:when test="$varDataElementType = 'DATE'">
                <xsl:variable name="varOrder"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varYear"><xsl:value-of select="Year" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <select name="Day{$varOrder}" class="uportal-input-text">
                                <xsl:for-each select="Day">
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="." />
                                    </option>
                                </xsl:for-each>
                            </select>
                           
                            <select name="Month{$varOrder}" class="uportal-input-text">
                                <xsl:for-each select="Month">
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="." />
                                    </option>
                                </xsl:for-each>
                            </select>
                            
                            <input type="text" name="Year{$varOrder}" value="{$varYear}" size="5" class="uportal-input-text" />
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            
            <!-- SYSTEM LOOKUP data element -->
            <xsl:when test="$varDataElementType = 'SYSTEM LOOKUP'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <select name="{$varOrder}" class="uportal-input-text" onChange="javascript:mySubmit('{$baseActionURL}?current=smartform_result_view&amp;domain={$domain}&amp;participant={$participant}')">
                                 <option value="">
                                    
                                  </option>
                                <xsl:for-each select="option">                                   
                                    <xsl:variable name="varoptiontooltip"><xsl:value-of select="optiontooltip" /></xsl:variable>
                                    <option title="testing">
                                        <xsl:attribute name="value"><xsl:value-of select="optionvalue" /></xsl:attribute>
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="optionlabel" />
                                    </option>
                                </xsl:for-each>
                            </select>                           
                        </td>   
                        <td width="5%"></td>               
                    </tr>
                    </table>
                    <!-- Additional Info Start -->
                    <table width="100%">
                    <xsl:for-each select="DisplayAddnlInfo">
                    <xsl:variable name="AddnlInfoLabel"><xsl:value-of select="AddnlInfoLabel" /></xsl:variable>
                    <xsl:variable name="AddnlInfoValue"><xsl:value-of select="AddnlInfoValue" /></xsl:variable>
                    
                    <tr>                        
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"></td>
                        <td width="45%" class="uportal-label">
                            &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
                            
                           
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                           
                            <xsl:value-of select="AddnlInfoLabel"/>:&#160;&#160;<xsl:value-of select="AddnlInfoValue"/>
                        </td>    
                        <td width="5%"></td>                 
                    </tr>
                    </xsl:for-each>
                    <!-- Additional Info End -->
                </table>
            </xsl:when>
            
            <!-- SCRIPT data element -->
            <xsl:when test="$varDataElementType = 'SCRIPT'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varValue"><xsl:value-of select="value" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <xsl:value-of select="value" />
                            <input type="hidden" name="{$varOrder}" value="{$varValue}" />
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            
            
            <!-- TEXT data element -->
            <xsl:when test="$varDataElementType = 'REPEATABLE TEXT'">
                <xsl:variable name="varRow"><xsl:value-of select="DATAELEMENTS_intDataElementRow" /></xsl:variable>
                <xsl:variable name="varColumn"><xsl:value-of select="DATAELEMENTS_intDataElementColumn" /></xsl:variable>
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varDEOrder"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varYear"><xsl:value-of select="Year" /></xsl:variable>
                
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            
                            <select name="{$varOrder}_Day" class="uportal-input-text">
                                <xsl:for-each select="Day">
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="." />
                                    </option>
                                </xsl:for-each>
                            </select>
                           
                            <select name="{$varOrder}_Month" class="uportal-input-text">
                                <xsl:for-each select="Month">
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="." />
                                    </option>
                                </xsl:for-each>
                            </select>
                            
                            <input type="text" name="{$varOrder}_Year" value="{$varYear}" size="5" class="uportal-input-text" />
                            &#160;&#160;&#160;&#160;
                            <textarea name="{$varOrder}" rows="{$varRow}" cols="{$varColumn}" class="uportal-input-text">
                                <xsl:value-of select="value" />
                            </textarea>
                            
                            
                            <input type="button" name="Add Repeatable DE" tabindex="2" value="Add" onClick="javascript:addRepeatableDE('{$varDEOrder}')" class="uportal-button" />
                        </td>
                        <td width="5%"></td>
                    </tr>
                    <xsl:for-each select="repeatedDEData">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"></td>
                        <td width="45%" class="uportal-label">
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">                                                
                                <xsl:variable name="index"><xsl:value-of select="index" /></xsl:variable>
                                <xsl:variable name="date"><xsl:value-of select="date" /></xsl:variable>
                                <input type="hidden" name="Date{$varOrder}_{$index}" value="{$date}" size="15" class="uportal-label">                                    
                                </input>
                                <xsl:value-of select="date" />
                                &#160;&#160;&#160;&#160;
                                <textarea name="Data{$varOrder}_{$index}" rows="{$varRow}" cols="{$varColumn}" class="uportal-input-text">
                                    <xsl:value-of select="data" />
                                </textarea>                                                        
                                
                                &#160;&#160;
                                <a href="javascript:removeRepeatableDE('{$varDEOrder}', '{$index}')"> 
                                        Del 
                                </a>
                        </td>    
                        <td width="5%"></td>
                    </tr>    
                    </xsl:for-each>
                </table>
            </xsl:when>
                        
            <!-- Attachment Dataelement Start -->
            
            <xsl:when test="$varDataElementType = 'ATTACHMENT'">
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <xsl:variable name="varValue"><xsl:value-of select="value" /></xsl:variable>
                <xsl:variable name="strResultKey"><xsl:value-of select="strResultKey" /></xsl:variable>
               
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="30%" class="uportal-label">
                            <input type="file" name="{$varOrder}" value="{$varValue}" size="20" class="uportal-input-text" style="text-align: left" />                            
                            <input type="hidden" name="attachHidden{$varOrder}" value="{$varValue}" />
                        </td>
                        <xsl:choose>                        
                            <xsl:when test="string-length($varValue) > '0' ">
                                <td width="9%" class="uportal-label">                            
                                    
                                     <a href="{$downloadURL}?uP_root={$nodeId}&amp;domain=SMARTFORMRESULTS&amp;primary_field=SMARTFORMRESULTS_intSmartformResultID&amp;primary_value={$strResultKey}&amp;file_name_field=SMARTFORMRESULTS_strDataElementResult&amp;property_name=neuragenix.genix.smartform.AttachmentLocation&amp;activity_required=smartform_complete" target="_blank"> 
                                     <!--<a href="{$downloadURL}?uP_root={$nodeId}&amp;file_name={$varValue}&amp;property_name=neuragenix.genix.smartform.AttachmentLocation&amp;activity_required=view_case_summary" target="_blank">  -->
                                        View &#160;&#160;
                                    </a>
                                    <a href="{$baseActionURL}?uP_root=root&amp;del=true&amp;current=smartform_result_view&amp;varDataElementID={$varDataElementID}&amp;deleteAttachment=true&amp;domain={$domain}&amp;participant={$participant}&amp;pagecount={$pagecount}&amp;currentpage={$currentpage}&amp;startorder={$startorder}&amp;endorder={$endorder}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$SMARTFORMPARTICIPANTS_intSmartformID}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$SMARTFORMPARTICIPANTS_intSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$SMARTFORMPARTICIPANTS_strSmartformStatus}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$SMARTFORMPARTICIPANTS_intCurrentPage}&amp;lookupReload={$lookupReload}"> 
                                        Del &#160;&#160;
                                    </a> 
                                </td>
                               
                            </xsl:when>
                            <xsl:otherwise>
                                <td width="9%"></td>
                            </xsl:otherwise>
                        </xsl:choose>                        
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>
            <!-- Attachment End -->
            
            <!-- FORM LINK data element -->
            <xsl:when test="$varDataElementType = 'FORM LINK'">
                <xsl:variable name="varRow"><xsl:value-of select="DATAELEMENTS_intDataElementRow" /></xsl:variable>
                <xsl:variable name="varColumn"><xsl:value-of select="DATAELEMENTS_intDataElementColumn" /></xsl:variable>
                <xsl:variable name="varPageName"><xsl:value-of select="DATAELEMENTS_strPageName" /></xsl:variable>
                <xsl:variable name="varChannelFunctionName"><xsl:value-of select="DATAELEMENTS_strChannelFunctionalName" /></xsl:variable>
                <xsl:variable name="varOrder">d<xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementOrder" /></xsl:variable>
                <table width="100%">
                    <tr>
                        <td width="8%"></td>
                        <td width="2%" class="uportal-label"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementNo" />.</td>
                        <td width="45%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTS_strDataElementName" />&#160;
                            <xsl:if test="$varDataElementMandatory = '-1'">
                                <span class="neuragenix-form-required-text"><b>*</b></span>
                            </xsl:if>
                            &#160;&#160;
                            
                            <xsl:if test="string-length(normalize-space($varDataElementHelp)) &gt; 0">
                                <img src="media/neuragenix/icons/help.gif" border="0" title="{$varDataElementHelp}" />
                            </xsl:if>
                        </td>
                        <td width="1%"></td>
                        <td width="39%" colspan="2" class="uportal-label">
                            <textarea name="{$varOrder}" rows="{$varRow}" cols="{$varColumn}" class="uportal-input-text">
                                <xsl:value-of select="value" />
                            </textarea>
                            
                            <xsl:choose>
                                <xsl:when test="$varChannelFunctionName = 'CPatient'">
                                  <input type="button" name="refer" value="Lookup" class="uportal-button" onclick="javascript:jumpToDiagnosis('{$patientChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;PATIENT_intInternalPatientID={$participant}&amp;action={$varPageName}&amp;currentField={$varOrder}&amp;ValueFromRuntimeData=true')" />
                                </xsl:when>
                                
                                <xsl:when test="$varChannelFunctionName = 'CBiospecimen'">
                                    <xsl:choose>
                                    <xsl:when test="(string-length( $fromSFBatchGeneration ) > 0)">                                
                                        <input type="button" name="refer" value="Lookup" class="uportal-button" onclick="javascript:jumpToDiagnosis('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;module=Picker&amp;action={$varPageName}&amp;currentField={$varOrder}&amp;ValueFromRuntimeData=true')" />
                                    </xsl:when>                                
                                    <xsl:otherwise>
                                        <input type="button" name="refer" value="Lookup" class="uportal-button" onclick="javascript:jumpToDiagnosis('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;BIOSPECIMEN_intBiospecimenID={$participant}&amp;module=Picker&amp;action={$varPageName}&amp;currentField={$varOrder}&amp;ValueFromRuntimeData=true')" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                
                                <xsl:when test="$varChannelFunctionName = 'CSmartform'">
                                  <input type="button" name="refer" value="Lookup" class="uportal-button" onclick="javascript:jumpToDiagnosis('{$baseActionURL}?uP_root=root&amp;current={$varPageName}&amp;currentField={$varOrder}&amp;ValueFromRuntimeData=true')" />
                                </xsl:when>

                                
                                <xsl:otherwise>
                                </xsl:otherwise>
                            </xsl:choose>
                            
                        </td>
                        <td width="5%"></td>
                    </tr>
                </table>
            </xsl:when>

        </xsl:choose>
        
    </xsl:for-each>
    
    <table width="100%">
        <tr>
            <td width="30%"></td>
            <td width="70%">
                <xsl:if test="$currentpage &gt; 1">
                <input type="submit" name="previous" tabindex="1" value="&lt; Previous" class="uportal-button" />
                </xsl:if>
                
                <input type="submit" name="save" tabindex="2" value="{$saveBtnLabel}" class="uportal-button" />
                
                <xsl:if test="$currentpage &lt; $pagecount">
                <input type="submit" name="next" tabindex="3" value="{$nextBtnLabel}" class="uportal-button" />
                </xsl:if>
                
                <xsl:if test="(string-length( $fromSFBatchGeneration ) = 0 and not(substring(fromPatientAnalysis,'true')))">
                    <xsl:if test="$currentpage = $pagecount">
                    <input type="submit" name="complete" tabindex="3" value="Save and close" class="uportal-button" />
                    </xsl:if>
                </xsl:if>
            </td>
        </tr>
    </table>

    <br/>
    <hr/>
    
    <xsl:if test="(string-length( $fromSFBatchGeneration ) > 0)">
        <table width="100%">
            <tr>
                <td align="left">
                        <xsl:choose>
                        <xsl:when test="string-length($fromBiospecimenKey) > 0">
                            <input type="button" name="Cancel" value="Cancel" class="uportal-button" onclick="javascript:jumpTo('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$fromBiospecimenKey}')"  />        
                        </xsl:when>
                        <xsl:otherwise>
                            <input type="button" name="Cancel" value="Cancel" class="uportal-button" onclick="javascript:jumpTo('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;current=search_biospecimen')"  />        
                        </xsl:otherwise>
                        </xsl:choose>
                </td>
                <td align="right">
                        <input type="button" name="Next" value="Next" class="uportal-button" onclick="javascript:jumpTo('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;module=BATCH_SFRESULTS_GENERATION&amp;action=GenerateSFResults')"  />        
                </td>
            </tr>
        </table>
    </xsl:if>    
    
    <!-- hidden fields to send data to server -->
    <input type="hidden" name="domain" value="{$domain}" />
    <input type="hidden" name="participant" value="{$participant}" />
    <input type="hidden" name="pagecount" value="{$pagecount}" />
    <input type="hidden" name="currentpage" value="{$currentpage}" />
    <input type="hidden" name="startorder" value="{$startorder}" />
    <input type="hidden" name="endorder" value="{$endorder}" />
    <input type="hidden" name="strTitle" value="{$strTitle}" />
    <input type="hidden" name="SMARTFORM_smartformname" value="{$SMARTFORM_smartformname}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_intSmartformID" value="{$SMARTFORMPARTICIPANTS_intSmartformID}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_intSmartformParticipantID" value="{$SMARTFORMPARTICIPANTS_intSmartformParticipantID}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_strSmartformStatus" value="{$SMARTFORMPARTICIPANTS_strSmartformStatus}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_intCurrentPage" value="{$SMARTFORMPARTICIPANTS_intCurrentPage}" />
    <input type="hidden" name="intStudyID" value="{$intStudyID}" />
    <input type="hidden" name="lookupReload" value="{$lookupReload}" />    
    <input type="hidden" name="AddRepeatableDE" value="" />
    <input type="hidden" name="RemoveRepeatableDE" value="" />
    <input type="hidden" name="RemoveRepeatableDEIndex" value="" />
    <input type="hidden" name="var1" value="{$var1}" />
    <input type="hidden" name="var2" value="{$var2}" />
    <input type="hidden" name="var3" value="{$var3}" />
    <input type="hidden" name="var4" value="{$var4}" />
    <input type="hidden" name="current" value="smartform_result_view" />
    <xsl:if test="(string-length( $fromPatient ) > 0)">
        <input type="hidden" name="fromPatient" value="true" />
    </xsl:if>
    <xsl:if test="(substring(fromPatientAnalysis,'true'))">
        <input type="hidden" name="fromPatientAnalysis" value="true" />
        <input type="hidden" name="PATIENT_intPatientID" value="{$PATIENT_intPatientID}" />
    </xsl:if>    
    <xsl:if test="(string-length( $fromSFBatchGeneration ) > 0)">
        <input type="hidden" name="fromSFBatchGeneration" value="true" />
    </xsl:if>
    <input type="hidden" name="fromBiospecimenKey" value="{$fromBiospecimenKey}" />
    <input type="hidden" name="PATIENT_strSurname" value="{$PATIENT_strSurname}" />
    <input type="hidden" name="PATIENT_strFirstName" value="{$PATIENT_strFirstName}" />
    
    </form>
    </xsl:template>

</xsl:stylesheet>
