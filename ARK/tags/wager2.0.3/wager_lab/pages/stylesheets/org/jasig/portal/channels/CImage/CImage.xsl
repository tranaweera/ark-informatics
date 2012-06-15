<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="content">

  <p align="center"><xsl:value-of select="caption"/></p>
  <p align="center">
    <a href="{image/@link}">
      <image src="{image/@src}" width="{image/@width}" height="{image/@height}" border="{image/@border}"/>
    </a>
  </p>
  <p align="center"><font size="2"><xsl:value-of select="subcaption"/></font></p>

</xsl:template>

</xsl:stylesheet> 
