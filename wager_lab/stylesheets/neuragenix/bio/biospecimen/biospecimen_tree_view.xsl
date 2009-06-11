<?xml version="1.0" encoding="utf-8"?>

<!-- Biospecimen Tree View 
     Rewritten for on the fly processing by Daniel Murley
     email : dmurley@neuragenix.com
     
     
     Copyright (C) 2005 Neuragenix Pty. Ltd.
     
     -->






<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./biospecimen_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

<xsl:output method="html" />
    <xsl:param name="studyChannelURL">studyChannelURL_false</xsl:param>
    <xsl:param name="patientChannelURL">patientChannelURL_false</xsl:param>
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="inventoryChannelTabOrder"></xsl:param>
    <xsl:param name="biospecimenChannelTabOrder"></xsl:param>
    <xsl:param name="patientChannelTabOrder"></xsl:param>
    <xsl:template match="biospecimen">
        <xsl:param name="intCurrentPage"><xsl:value-of select="intCurrentPage" /></xsl:param>
        <xsl:param name="intRecordPerPage"><xsl:value-of select="intRecordPerPage" /></xsl:param>
        <xsl:param name="intMaxPage"><xsl:value-of select="intMaxPage" /></xsl:param>
        <xsl:param name="blBackBioButton"><xsl:value-of select="blBackBioButton" /></xsl:param>
        <xsl:param name="callingDomain"><xsl:value-of select="callingDomain" /> </xsl:param>
        <xsl:variable name="addpatientbutton" select="(count(addpatientbutton)=1)" />
	<xsl:variable name="addpatientbuttonid" select="addpatientbutton" />
        <xsl:variable name="currentPage" select="currentPage" />
        <xsl:variable name="intInternalPatientID" select="PATIENT_intInternalPatientID" />
        <xsl:variable name="searchResultPage" select="searchResultPage" />
        <xsl:variable name="intStudyID" select="intStudyID" />

        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">Search results</td>
                <td align="right">

                <xsl:if test="$callingDomain='patient'">

                    <form name="back_form" action="{$patientChannelURL}?uP_root=root" method="POST">
                        <input type="hidden" name="uP_sparam" value="activeTab" />
                        <input type="hidden" name="activeTab" value="1" />
                        <input type="hidden" name="current" value="patient_results" />
                        <input type="hidden" name="PATIENT_intInternalPatientID" value="{$intInternalPatientID}" />
                        <input type="hidden" name="action" value="view_patient"/>
                        
                        
                        
                        <!--<input type="hidden" name="submit" value="bioSpecimen" /> -->
                        <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                            alt="Previous" onclick="javascript:document.back_form.submit();"/>
                        <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                            alt="Next"/>
                    </form>

                </xsl:if>

                <xsl:if test="$currentPage='study_view'">

                    <form name="back_form" action="{$studyChannelURL}?uP_root=root" method="POST">
                        <input type="hidden" name="uP_sparam" value="activeTab" />
                        <input type="hidden" name="activeTab" value="2" />
                        <input type="hidden" name="current" value="study_results" />
                        <input type="hidden" name="intStudyID" value="{$intStudyID}" />
                        <input type="hidden" name="submit" value="bioSpecimen" />
                        <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                            alt="Previous" onclick="javascript:document.back_form.submit();"/>
                        <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                            alt="Next"/>
                        <!-- <input type="submit" value="&lt; Back" class="uportal-button" /> -->
                    </form>

                </xsl:if>
                   

                </td>
            </tr>

        </table>

        <table width = "100%">
            <tr>
                <td>
                    <hr />
                </td>
            </tr>
        </table>


        <xsl:if test="$callingDomain='patient'">	

            <table width="100%">
		<tr>
                    <td width="10%" />			
                    <td width="15%" class="uportal-label"><xsl:value-of select="PATIENT_strPatientIDDisplay" />: </td>
                    <td width="15%" class="uportal-text"><xsl:value-of select="PATIENT_strPatientID" /></td>
                    <td width="5%"></td>
                    <td width="15%" class="uportal-label"><xsl:value-of select="PATIENT_dtDobDisplay" />: </td>
		    
		    <td width="15%" class="uportal-text" ><xsl:value-of select="PATIENT_dtDob" /></td>
                    <td width="25%"/>		
		</tr>

		<tr> 
                    <td width="10%"/>		
                    <td width="15%" class="uportal-label"><xsl:value-of select="PATIENT_strFirstNameDisplay" />: </td>
                    <td width="15%" class="uportal-text"><xsl:value-of select="PATIENT_strFirstName" /></td>
                    <td width="5%"></td>
                    <td width="15%" class="uportal-label"><xsl:value-of select="PATIENT_strSurnameDisplay" />:</td>
                    <td width="15%" class="uportal-text"><xsl:value-of select="PATIENT_strSurname" /></td>
                    <td width="25%"/>		
		</tr>
            </table>

            <table width = "100%">
                <tr>
                    <td>
                        <hr />
                    </td>
                </tr>
            </table>

            <table width="100%"> 
		<tr>  
                    <td width="3%"></td>
                    <td width="97%">
                        <xsl:choose>
                            <xsl:when test="hasCollection='false'">
                                <font color="RED">There are no collections added for this patient - No biospecimens can be added!</font>
                            </xsl:when>
                            <xsl:otherwise>
                                <a href="{$baseActionURL}?module=core&amp;action=add_biospecimen&amp;PATIENT_intInternalPatientID={PATIENT_intInternalPatientID}&amp;BIOSPECIMEN_intPatientID={PATIENT_intInternalPatientID}">
                                    Add New Biospecimen
                                </a>
                                <a href="/wagerlab/DefaultBarcode.prn?patientkey={PATIENT_intInternalPatientID}">
                                    Print all barcodes
                                </a>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
		</tr>
            </table>

            <table width = "100%">
                <tr>
                    <td>
                        <hr />
                    </td>
                </tr>
            </table>

        </xsl:if>

        <xsl:if test="not(hasCollection='false')">
        
        <table width="100%">
 
            <tr>
                <td width="2%" />
                <td width="18%" class="uportal-label"><xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay" /></td>
                <td width="10%" class="uportal-label"><xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay" /></td>
                <td width="15%" class="uportal-label"><xsl:value-of select="BIOSPECIMEN_dtSampleDateDisplay" /></td>
                <td width="10%"  class="uportal-label" align="center">Qty. Avail</td>
                <td width="25%"  class="uportal-label">Location</td>
            </tr>

           
                <xsl:for-each select="node">
                    
                
                <!--- DEBUG PRINTS : REMOVE ME ! -->
                <!--
                    <tr>
                        <xsl:variable name="level">
                            <xsl:value-of select="./@level" />
                        </xsl:variable>
                        <xsl:variable name="internalIndex">
                            <xsl:value-of select="./@internalIndex" />
                        </xsl:variable>
                        
                        
                        <td colspan="8">
                            <xsl:variable name="bioID"><xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></xsl:variable>
                                |<xsl:call-template name="spacer">
                                    <xsl:with-param name="spacerAmount" select="number($level) + 3" />
                                </xsl:call-template>|
                            
                            <xsl:if test="HASCHILDREN='true'">
                               <a href="{$baseActionURL}?uP_root=root&amp;module=biospecimen_search&amp;action=open_node&amp;BIOSPECIMEN_intBiospecimenID={$bioID}&amp;expandID={$internalIndex}">opennode</a>, 
                            </xsl:if>
                            
                            <a href="{$baseActionURL}?uP_root=root&amp;module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$bioID}"><xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></a>, <xsl:value-of select="BIOSPECIMEN_strBiospecimenID" />, ii: <xsl:value-of select="$internalIndex"/>, <xsl:value-of select="$level" />
                        </td>
                        
                    </tr>
                    
                    
                    <xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></a>, <xsl:value-of select="BIOSPECIMEN_strBiospecimenID" />, ii: <xsl:value-of select="$internalIndex"/>, <xsl:value-of select="$level" />
                    -->
                    
                    
                    
                    <!-- END DEBUG -->
                    
                    
                    
                    <tr class="uportal-input-text">
                        <xsl:variable name="classIDNumber"><xsl:number level="any" count="node" /></xsl:variable>
                            <xsl:choose>
                               <xsl:when test="$classIDNumber mod 2 != 0">
                                  <xsl:attribute name="class">uportal-input-text</xsl:attribute> 
                               </xsl:when>
                               <xsl:otherwise>
                                  <xsl:attribute name="class">uportal-text</xsl:attribute> 
                               </xsl:otherwise>
                            </xsl:choose>
                    
                    
                    
                    
                    
                        <xsl:variable name="level">
                            <xsl:value-of select="./@level" />
                        </xsl:variable>
                        <xsl:variable name="internalIndex">
                            <xsl:value-of select="./@internalIndex" />
                        </xsl:variable>
                        
                        <xsl:variable name="bioID">
                            <xsl:value-of select="BIOSPECIMEN_intBiospecimenID" />
                        </xsl:variable>
                        
                        <xsl:variable name="intPatientID">
                            <xsl:value-of select="BIOSPECIMEN_intPatientID" />
                        </xsl:variable>
                        
                        
                        <td align="left">
                           <!-- biospecimen ID and arrow -->
                            <xsl:call-template name="spacer">
                                <xsl:with-param name="spacerAmount" select="number($level)" />
                            </xsl:call-template>
                        
                            <xsl:choose>
                               <xsl:when test="HASCHILDREN='true'">
                                   <!-- depth check needed here -->
                                   <xsl:choose>
                                       <xsl:when test="./@openNode='true'">
                                           <img src="media/neuragenix/buttons/blue_arrow_open.gif" width="12" height="8" />  
                                       </xsl:when>
                                       <xsl:otherwise>
                                           <a href="{$baseActionURL}?uP_root=root&amp;module=biospecimen_search&amp;action=open_node&amp;BIOSPECIMEN_intBiospecimenID={$bioID}&amp;expandID={$internalIndex}&amp;PAGING_currentPage={$intCurrentPage}">
                                               <img src="media/neuragenix/buttons/blue_arrow_close.gif" width="8" height="12" border="0" />  
                                           </a>
                                       </xsl:otherwise>
                                   </xsl:choose>
                               </xsl:when>
                               <xsl:otherwise>
                                   <img src="media/neuragenix/icons/bullet.gif" width="16" height="16" />
                               </xsl:otherwise>
                            </xsl:choose>
                            
                        </td>
                        
                        <td>
                            <a href="{$baseActionURL}?uP_root=root&amp;module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$bioID}">
                                <xsl:value-of select="BIOSPECIMEN_strBiospecimenID" />
                            </a>
                        </td>
                        
                        <td>
                            <xsl:value-of select="BIOSPECIMEN_strSampleType" /> 
                        </td>
                        
                        
                        <td>
                           <xsl:value-of select="BIOSPECIMEN_dtSampleDate" />
                        </td>
                        
                        
                        <td align="center">
			<xsl:variable name="qty_avail">
			<xsl:value-of select="BIOSPECIMEN_flNumberRemoved + BIOSPECIMEN_flNumberCollected" />
			</xsl:variable>
			<xsl:choose>
			<xsl:when test="number(BIOSPECIMEN_flNumberCollected) and number(BIOSPECIMEN_flNumberRemoved)">
			    <xsl:value-of select="format-number(BIOSPECIMEN_flNumberCollected + BIOSPECIMEN_flNumberRemoved,'#.00')"/>	
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>	
					<xsl:when test="number(BIOSPECIMEN_flNumberCollected)">
					<xsl:value-of select="format-number(BIOSPECIMEN_flNumberCollected,'#.00')"/>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>			
			</xsl:choose>		
                       </td>
			 <td>
                        
                        
                           <xsl:variable name="intCellID"><xsl:value-of select="BIOSPECIMEN_intCellID" /></xsl:variable>                        
                           <xsl:if test="not(number($intCellID)=-1)">
                              <a href="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;current=view_tray&amp;source=view&amp;intInternalPatientID={$intPatientID}&amp;intBiospecimenID={$bioID}&amp;CELL_intCellID={$intCellID}&amp;strCurrent=biospecimen_view">
                              view
                              </a>
                           </xsl:if>
                        </td>
                        
                    </tr>
                    
                    
                </xsl:for-each>
                   
            
            
        </table>

       
        
        
        
        
        
        

		<table width = "100%">
			<tr>
				<td>
					<hr />
				</td>
			</tr>
		</table>
        <table>

            <script language="javascript">
            function submitPagingWithAction(action)
            {
               document.paging.action.value = action;
               document.paging.submit();
            
            
            }
            
            
            </script>
        
     
        
            <form action="{$baseActionURL}?current=biospecimen_tree_view" method="post" name="paging">
                  <!-- <input type="hidden" name="PAGING_currentPage" value="{$currentPage}" /> -->
                  <input type="hidden" name="module" value="biospecimen_search" />
                  <input type="hidden" name="action" value="" />
                <input type="hidden" name="PAGING_intOldTotalPages"  value="{number($intCurrentPage) + 1}"/>
                 <input type="hidden" name="PAGING_intOldRecordPerPage" value="{$intRecordPerPage}"/>
            <tr>
				<td>
                    <table width="100%">
                        <tr>          
                            <td class="uportal-label">page:</td>
                            <td class="uportal-label">
                                <input type="button" name="previous" value="{$ltBtnLabel}" tabindex="1" class="uportal-button"  onClick="javascript:submitPagingWithAction('PAGING_previousPage');">
                                    <xsl:if test="$intCurrentPage=0">
                                        <xsl:attribute name="disabled">
                                           true
                                        </xsl:attribute>
                                    </xsl:if>
                                 </input>
                            </td>
                            <td  class="uportal-label">
			       <input type="hidden" name="PAGING_currentPage" value="{$intCurrentPage}" />
                               <input type="text" name="currentPage" size="4" tabindex="2" value="{number($intCurrentPage) + 1}" align="right" class="uportal-input-text" />
                            </td>
                            <td class="uportal-label">of <xsl:value-of select="number(intMaxPage) + 1" /></td>
                            <td  class="uportal-label">
                                <input type="button" name="next" value="{$gtBtnLabel}" tabindex="3" class="uportal-button" onClick="javascript:submitPagingWithAction('PAGING_nextPage')">
                                    <xsl:if test="number($intCurrentPage)=number($intMaxPage)">
                                        <xsl:attribute name="disabled">
                                            disabled
                                        </xsl:attribute>
                                    </xsl:if>
                                 </input>
                            </td>
                            <td  class="uportal-label">
                                <input type="button" name="go" value="{$goBtnLabel}" tabindex="4" class="uportal-button" onClick="javascript:submitPagingWithAction('PAGING_setPage')"/>
                            </td>                           
                            <td></td>
                            <td  class="uportal-label">Display</td>
                            <td  class="uportal-label">
				<input type="text" name="PAGING_pagingSize" size="4" tabindex="5" value="{$intRecordPerPage}" align="right" class="uportal-input-text" />
                            </td>
                            <td class="uportal-label">records at a time</td>
                            <td class="uportal-label">
                                <input type="button" name="set" value="{$setBtnLabel}" tabindex="6" class="uportal-button" onClick="javascript:submitPagingWithAction('PAGING_setPagingSize');"/>
                            </td>
                            <td></td>
                            <td class="uportal-label">
                                    <input type="button" name="search" value="Reset Search" tabindex="7" class="uportal-button" onClick="javascript:window.location='{$baseActionURL}?current=biospecimen_search&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenChannelTabOrder}&amp;uP_root=root&amp;reset=1'"/>
                            </td>
                            <td></td>
                        </tr>
                    </table>
				</td>
			</tr>
		</form>
               
            </table>
       </xsl:if>
</xsl:template>




<xsl:template name="spacer">
   <xsl:param name="spacerAmount">0</xsl:param>
   <xsl:if test="$spacerAmount&gt;0">
      <img src="media/neuragenix/icons/dtree/empty.gif" width="16" height="16"/>
      <xsl:call-template name="spacer">
         <xsl:with-param name="spacerAmount" select="number($spacerAmount)-1"/>
      </xsl:call-template>
   </xsl:if>
   
</xsl:template>








    <!-- avoid output of text node with default template -->
    <xsl:template match="strBiospecimenID"/>
    <xsl:template match="intBiospecimenID"/>
    <xsl:template match="addpatientbutton"/>
    <xsl:template match="currentPage"/>
    <xsl:template match="intInternalPatientID"/>
    <xsl:template match="intStudyID"/>
    <xsl:template match="strBiospecSampleType"/>
    <xsl:template match="strBiospecSampleSubType"/>
    <xsl:template match="dtBiospecSampleDate"/>
    <xsl:template match="intBiospecNumCollected"/>
    <xsl:template match="intBiospecNumRemoved"/>
    <xsl:template match="PATIENT_Timestamp"/>
    <xsl:template match="strSurname"/>
    <xsl:template match="strPatientID"/>
    <xsl:template match="strFirstName"/>
    <xsl:template match="dtDob"/>
    <xsl:template match="strHospitalUR"/>
    <xsl:template match="intInternalPatientID"/>
    <xsl:template match="strSurnameDisplay"/>
    <xsl:template match="strPatientIDDisplay"/>
    <xsl:template match="strFirstNameDisplay"/>
    <xsl:template match="dtDobDisplay"/>
    <xsl:template match="strHospitalURDisplay"/>
    <xsl:template match="intInternalPatientIDDisplay"/>
    <xsl:template match="intCurrentPage"/>
    <xsl:template match="intRecordPerPage"/>
    <xsl:template match="intMaxPage"/>
    <xsl:template match="blBackBioButton"/>
    <xsl:template match="strOtherID"/>
    <xsl:template match="strOtherIDDisplay"/>
    <xsl:template match="flagged"/>  
    <xsl:template match="intCellID"/>
    <xsl:template match="intStudyID"/>
    <xsl:template match="searchResultPage"/>
    <xsl:template match="strComments"/> 
    <xsl:template match="strCommentsDisplay"/> 
    <xsl:template match="strEncounter"/> 
    <xsl:template match="strSelectedEncounter"/> 
    <xsl:template match="strEncounterDisplay"/> 
    <xsl:template match="search_encounter_list"/>
 
</xsl:stylesheet>
