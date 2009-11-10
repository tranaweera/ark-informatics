<?xml version="1.0" encoding="utf-8"?>

<!-- 
    smartform_list.xsl, part of the Smartform channel
    Author: hhoang@neuragenix.com
    Date: 26/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../../common/common_btn_name.xsl"/>
    
    <xsl:param name="formParams">current=smartform_list</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="caseChannelURL">caseChannelURL_false</xsl:param>
    <xsl:param name="caseChannelTabOrder">caseChannelTabOrder</xsl:param>
    <xsl:param name="patientChannelURL">patientChannelURL_false</xsl:param>
    <xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
    <xsl:param name="BiospecimenChannelURL">BiospecimenChannelURL_false</xsl:param>
    <xsl:param name="BiospecimenChannelTabOrder">BiospecimenChannelTabOrder</xsl:param>
    

    <xsl:template match="smartform">
    
    <xsl:param name="SMARTFORMPARTICIPANTS_strUserNote"><xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNote" /></xsl:param>
    <xsl:param name="domain"><xsl:value-of select="domain" /></xsl:param>
    <xsl:param name="intStudyID"><xsl:value-of select="intStudyID" /></xsl:param>
    <xsl:param name="participant"><xsl:value-of select="participant" /></xsl:param>
    <xsl:param name="strTitle"><xsl:value-of select="strTitle" /></xsl:param>
    <xsl:param name="strBackButton"><xsl:value-of select="strBackButton" /></xsl:param>
    
    <script language="javascript" >

	function confirmDelete(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		getTextValue(aURL);
            }
	}
        
        function jumpTo(aURL){
            window.location=aURL;
        }
        
        function getTextValue(aURL){
            
            aURL = aURL + '&amp;SMARTFORMPARTICIPANTS_strUserNote=';
            jumpTo(aURL);
        }

        
    </script>
    
    <form name="myForm" action="{$baseActionURL}?{$formParams}" method="post">
    
    <table width="100%">
        <tr>
            <td width="10%"></td>
            <td width="2px" bgcolor="black"></td>
            <td width="1%"></td>
            <td width="20%" class="uportal-label">
                <xsl:choose>
                    <xsl:when test="$domain = 'CASE'">
                        Activity Details
                    </xsl:when>
                    <xsl:when test="$domain = 'Study'">
                        Biospecimen Analysis/Study Details
                    </xsl:when>
                    <xsl:when test="$domain = 'Admissions'">
                        Collection Details
                    </xsl:when>
                    <xsl:when test="$domain = 'Biospecimen'">
                        Biospecimen Analysis/Study Details
                    </xsl:when>
                    <xsl:when test="$domain = 'Bioanalysis'">
                        Biospecimen Analysis/Study Details
                    </xsl:when>
                </xsl:choose>            

            </td>
            <td width="44%" class="neuragenix-form-required-text">
                <xsl:value-of select="strErrorMessage" />
            </td>
            <td width="2px" bgcolor="black"></td>
            <td width="1%"></td>
            <td width="19%" class="uportal-label">
                <xsl:choose>
                    <xsl:when test="$domain = 'Admissions'">
                            Collection Group ID:
                    </xsl:when>
                     <xsl:otherwise>
                            ID:
                     </xsl:otherwise>
                </xsl:choose>                
                <xsl:value-of select="strTitle" /><!--xsl:value-of select="participant" /-->
            </td>
            
            <td width="5%">
                <xsl:choose>
                <xsl:when test="$domain = 'CASE'">
                    <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$caseChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$caseChannelTabOrder}{$strBackButton}')" />
                </xsl:when>
                <xsl:when test="$domain = 'Study'">
                        <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$patientChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;{$strBackButton}')" />
                </xsl:when>
                <xsl:when test="$domain = 'Admissions'">
                        <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$patientChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;{$strBackButton}')" />
                </xsl:when>
                <xsl:when test="$domain = 'Biospecimen'">
                        <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;{$strBackButton}')" />
                </xsl:when>
                <xsl:when test="$domain = 'Bioanalysis'">
                        <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$BiospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$BiospecimenChannelTabOrder}&amp;{$strBackButton}')" />
                </xsl:when>
                </xsl:choose>
            </td>
        </tr>
        
    </table>
    <hr/>
    
    <table width="100%" cellspacing="0" cellpadding="3">
        <tr>    
            <td width="20%" class="uportal-channel-table-header">
                  <xsl:choose>
                     <xsl:when test="$domain='Bioanalysis'">
                        Bioanalysis
                     </xsl:when>
                     <xsl:when test="$domain='Admissions'">
                        Collections
                     </xsl:when>
                     <xsl:otherwise>
                        Activity details
                     </xsl:otherwise>
                  </xsl:choose>
            </td>
            <xsl:choose>
                <xsl:when test="($domain = 'Bioanalysis') or ($domain='Admissions')">
                    
                    <td width="10%" class="uportal-channel-table-header">
                        Question1
                    </td>
                    <td width="10%" class="uportal-channel-table-header">
                        Question2
                    </td>
                    <td width="10%" class="uportal-channel-table-header">
                        Question3
                    </td>
                    <td width="10%" class="uportal-channel-table-header">
                        Question4
                    </td>
                    
                </xsl:when>
                
                <xsl:otherwise>
                    <td width="10%" class="uportal-channel-table-header">
                        <xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatusDisplay" />
                    </td>
                    <td width="70%" class="uportal-channel-table-header">
                    </td>    
                </xsl:otherwise>
                
             </xsl:choose>
            
        </tr>
        
        <tr>
            <td height="10px"></td>
        </tr>
        
        <xsl:for-each select="search_smartform_participant">
           <xsl:variable name="varSmartformParticipantID"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformParticipantID" /></xsl:variable>
           <xsl:variable name="varSmartformID"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" /></xsl:variable>
           <xsl:variable name="varCurrentPage"><xsl:value-of select="SMARTFORMPARTICIPANTS_intCurrentPage" /></xsl:variable>
           <xsl:variable name="varUserNote"><xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNote" /></xsl:variable>
           <xsl:variable name="varSmartformStatus"><xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus" /></xsl:variable>
           <xsl:variable name="varBackgroundColor"><xsl:value-of select="SMARTFORM_rowBackgroundColour" /></xsl:variable>
           <xsl:variable name="varRowColor"><xsl:if test="$varBackgroundColor='YELLOW'">FBFFBA</xsl:if><xsl:if test="$varBackgroundColor='BLUE'"></xsl:if></xsl:variable>
        
        
                <xsl:variable name="varSmartformName"><xsl:value-of select="SMARTFORM_strSmartformName" /></xsl:variable>
                
            <tr bgcolor="{$varRowColor}">
        
        
            <td width="10%" class="uportal-label">
                <a href="{$baseActionURL}?current=smartform_result_view&amp;domain={$domain}&amp;participant={$participant}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}&amp;intStudyID={$intStudyID}&amp;SMARTFORM_smartformname={$varSmartformName}">
                      <xsl:value-of select="SMARTFORM_strSmartformName" />
                </a>
            </td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="SMARTFORMRESULTS_strDataElementResult1" />
            </td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="SMARTFORMRESULTS_strDataElementResult2" />
            </td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="SMARTFORMRESULTS_strDataElementResult3" />
            </td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="SMARTFORMRESULTS_strDataElementResult4" />
            </td>
            
            <td width="70%" class="uportal-label">
              <xsl:if test="($domain='Bioanalysis') or ($domain='Admissions')">
                 <xsl:if test="SMARTFORM_strSmartformName != ''">               
                    <a href="javascript:confirmDelete('{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;delete=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                       Del
                    </a>
                 </xsl:if>
              </xsl:if>
                
            </td>
           </tr>
           
        </xsl:for-each>
    </table>
    <hr/>
    
    
    <xsl:if test="($domain = 'Bioanalysis') or ($domain='Admissions')">

        <table width="100%">
            <tr>    
                <td colspan="3" class="uportal-channel-table-header">
                    Add new smart form
                </td>
            </tr>
            <tr>
                <td width="20%">
                    <select name="SMARTFORMPARTICIPANTS_intSmartformID" tabindex="1" class="uportal-input-text">
                        <xsl:for-each select="search_smartform">

                            <option>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="SMARTFORM_intSmartformID" />
                                </xsl:attribute> 
                                <xsl:value-of select="SMARTFORM_strSmartformName" />
                            </option>

                        </xsl:for-each>
                    </select>
                </td>
                <td width="15%">
              
                </td>
                <td width="65%"></td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <td width="100%">
                    <input type="submit" name="add" tabindex="3" value="{$addBtnLabel}" class="uportal-button" />
                </td>
            </tr>
        </table>
    </xsl:if>
    <!-- hidden fields to send data to server -->
    <input type="hidden" name="domain" value="{$domain}" />
    <input type="hidden" name="participant" value="{$participant}" />
    <input type="hidden" name="intStudyID" value="{$intStudyID}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_strDomain" value="{$domain}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_intParticipantID" value="{$participant}" />
    
    </form>
    </xsl:template>

</xsl:stylesheet>
