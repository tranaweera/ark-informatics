<?xml version='1.0' encoding='utf-8' ?>
<!--
Copyright (c) 2001 The JA-SIG Collaborative.  All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in
   the documentation and/or other materials provided with the
   distribution.
   
3. Redistributions of any form whatsoever must retain the following
   acknowledgment:
   "This product includes software developed by the JA-SIG Collaborative
   (http://www.jasig.org/)."
   
THIS SOFTWARE IS PROVIDED BY THE JA-SIG COLLABORATIVE "AS IS" AND ANY
EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JA-SIG COLLABORATIVE OR
ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.

Author: Ken Weiner, kweiner@interactivebusiness.com
$Revision: 1.1 $
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>
  <xsl:param name="baseActionURL">default</xsl:param>
  <xsl:param name="unauthenticated">false</xsl:param>
<xsl:variable name="mediaPath" select="'media/org/jasig/portal/channels/CLogin'"/>
  <xsl:template match="login-status">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <xsl:choose>
        <xsl:when test="$unauthenticated='true'">
          <form action="Authentication" method="post">
            <input type="hidden" name="action" value="login"/>
            <xsl:call-template name="buildTable"/>
          </form>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="buildTable"/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="$unauthenticated='true'">
      </xsl:if>
    </table>
  </xsl:template>
  <xsl:template match="failure">
    <tr class="uportal-background-light">
      <td width="100%" class="uportal-channel-warning" colspan="1" rowspan="1">The user name/password combination entered is not recognized. Please try again.<span class="uportal-channel-warning"></span>
      </td>
      <td colspan="1" rowspan="1">
        <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
      </td>
    </tr>
  </xsl:template>
  <xsl:template match="error">
    <tr class="uportal-background-light">
      <td width="100%" class="uportal-channel-warning" colspan="1" rowspan="1">An error occured during authentication. The portal is unable to log you on at this time. Try again later.<span class="uportal-channel-warning"></span>
      </td>
      <td colspan="1" rowspan="1">
        <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
      </td>
    </tr>
  </xsl:template>
  <xsl:template match="full-name">
  </xsl:template>
  <xsl:template name="buildTable">
    <tr>
      <td colspan="1" rowspan="1">
        <img alt="interface image" src="{$mediaPath}/transparent.gif" width="5" height="5"/>
      </td>
    </tr>
    <tr>
      <td colspan="1" rowspan="1">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td colspan="4" class="uportal-background-light" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td rowspan="4" colspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="5" height="5"/>
            </td>
            <td class="uportal-background-light" colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td class="uportal-background-light" colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
          </tr>
          <tr>
            <td colspan="3" nowrap="nowrap" class="uportal-background-light" rowspan="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr class="uportal-background-light">
                  <td class="uportal-channel-text" nowrap="nowrap" colspan="1" rowspan="1">
                    <strong>
                      <xsl:choose>
                        <xsl:when test="$unauthenticated='true'">Welcome Guest - Please Login: </xsl:when>
                        <xsl:otherwise>Welcome <xsl:value-of select="full-name"/> </xsl:otherwise>
                      </xsl:choose>
                    </strong>
                  </td>
                </tr>
              </table>
            </td>
            <td nowrap="nowrap" class="uportal-background-light" colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td nowrap="nowrap" class="uportal-background-light" rowspan="2" colspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td nowrap="nowrap" class="uportal-background-light" colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td width="100%" class="uportal-channel-text" colspan="1" rowspan="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr class="uportal-background-light">
                  <td width="100%" class="uportal-channel-text" nowrap="nowrap" colspan="1" rowspan="1">
                    <!--Right Content Cell [1]-->
                    <xsl:choose>
                      <xsl:when test="$unauthenticated='true'">
                        <span class="uportal-label">Name:<img alt="interface image" src="{$mediaPath}/transparent.gif" width="4" height="1"/><input class="uportal-input-text" type="text" name="userName" size="15">
                            <xsl:attribute name="value">
                              <xsl:value-of select="/login-status/failure/@attemptedUserName"/>
                            </xsl:attribute></input>Password:<img alt="interface image" src="{$mediaPath}/transparent.gif" width="4" height="1"/><input class="uportal-input-text" type="password" name="password" size="15"/><img alt="interface image" src="{$mediaPath}/transparent.gif" width="4" height="1"/><input type="submit" value="Login" name="Login" class="uportal-button"/></span>
                      </xsl:when>
                      <xsl:otherwise>You are currently logged in.</xsl:otherwise>
                    </xsl:choose>
                  </td>
                  <td class="uportal-text-small" nowrap="nowrap" colspan="1" rowspan="1">
                    <xsl:choose>
                      <xsl:when test="$unauthenticated='true'">
                        <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
                      </xsl:when>
                      <xsl:otherwise>

                        <SCRIPT LANGUAGE="JavaScript1.2">


var months=new Array(13);
months[1]='January';
months[2]='February';
months[3]='March';
months[4]='April';
months[5]='May';
months[6]='June';
months[7]='July';
months[8]='August';
months[9]='September';
months[10]='October';
months[11]='November';
months[12]='December';
var time=new Date();
var lmonth=months[time.getMonth() + 1];
var date=time.getDate();
var year=time.getYear();
if (year &lt; 1000) {year = 1900 + year;} 
document.write(lmonth + ' ');
document.write(date + ', ' + year);
              </SCRIPT>
                      </xsl:otherwise>
                    </xsl:choose>
                  </td>
                </tr>
                <!--Right Message Row -->
                <xsl:if test="$unauthenticated='true'">
                  <xsl:apply-templates/>
                </xsl:if>
              </table>
            </td>
          </tr>
          <tr class="uportal-background-light">
            <td colspan="4" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
          </tr>
          <tr class="uportal-background-med">
            <td colspan="5" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
            <td colspan="1" rowspan="1">
              <img alt="interface image" src="{$mediaPath}/transparent.gif" width="1" height="1"/>
            </td>
          </tr>
        </table>
        <img alt="interface image" src="{$mediaPath}/transparent.gif" width="5" height="5"/>
      </td>
    </tr>
    <tr>
      <td colspan="1" rowspan="1">
        <img alt="interface image" src="{$mediaPath}/transparent.gif" width="5" height="5"/>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>

