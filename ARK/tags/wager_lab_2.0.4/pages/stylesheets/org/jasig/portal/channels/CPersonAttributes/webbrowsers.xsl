<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:param name="baseActionURL">default</xsl:param>

  <xsl:template match="/">
    <p class="uportal-text">
      uPortal channels have access to user attributes
      via the <span class="uportal-channel-code">org.jasig.portal.security.IPerson</span> object.
      Attribute names are defined in the 
      <a href="http://www.educause.edu/eduperson/">eduPerson object class</a> version 1.0.
    </p>
    <p>
      uPortal implementors are to map these standard attribute names to
      local names in their person directory or database.  Mappings are contained in
      the <span class="uportal-channel-code">properties/PersonDirs.xml</span> file.
    </p>
    <xsl:apply-templates select="attributes"/>
  </xsl:template>

  <xsl:template match="attributes">
    <table border="0" cellpadding="2" cellspacing="3">
      <tr class="uportal-background-med">
        <th>Att. Name</th>
        <th>Att. Value</th>
      </tr>
      <tr class="uportal-background-light">
        <td colspan="2">Available attributes:</td>
      </tr>
      <xsl:apply-templates select="attribute" mode="defined"/>
      <tr class="uportal-background-light">
        <td colspan="2">Unavailable attributes:</td>
      </tr>
      <xsl:apply-templates select="attribute" mode="undefined"/>
    </table>
  </xsl:template>

  <xsl:template match="attribute" mode="defined">
    <xsl:if test="value">
      <tr>
        <td><xsl:value-of select="name"/></td>
        <td><xsl:value-of select="value"/></td>
      </tr>
    </xsl:if>
  </xsl:template>

  <xsl:template match="attribute" mode="undefined">
    <xsl:if test="not(value)">
      <tr>
        <td><xsl:value-of select="name"/></td>
        <td>[Not available]</td>
      </tr>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet> 
