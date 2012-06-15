<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=add_box&amp;save=save</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="inventory">
       <script language="javascript">        
             // InvContainer OBJECT
        function InvContainer (ID, Name, Type)
        {
           // Properties
           this.ID = ID;
           this.Name = Name;
           this.Type = Type;
           this.subContainers = new Array();

           // Method Declarations

           this.addSubContainer = InvContainer_addSubContainer;
           this.getSubContainer = InvContainer_getSubContainer;
           this.getID = InvContainer_getID;
           this.getName = InvContainer_getName;
           this.getSelected = InvContainer_getSelected;
           this.setSelected = InvContainer_setSelected;
           this.getAvailableSpaces = InvContainer_getAvailableSpaces;
           this.setAvailableSpaces = InvContainer_setAvailableSpaces;

        }

        function InvContainer_addSubContainer(container)
        {
           this.subContainers[this.subContainers.length] = container;
        }

        function InvContainer_getSubContainer(container)
        {
           for (var i=0; i &lt; this.subContainers.length; i++)
           {
              if (this.subContainers[i].getID() == container)
                 return this.subContainers[i];
           }
           return null;
        }


        function InvContainer_getID()
        {
           return this.ID;
        }

        function InvContainer_getName()
        {
           return this.Name;
        }
        function InvContainer_getSelected()
        {
            return this.selected;
        }
        function InvContainer_setSelected(select)
        {
            this.selected = select;
        }
        function InvContainer_setAvailableSpaces(avSpace)
        {
           this.availableSpaces = avSpace;
        }

        function InvContainer_getAvailableSpaces()
        {
           return availableSpaces;
        }


        // ---- end Object












        var myInventory = new Array();

        function buildInventory(siteID, siteName, tankID, tankName, boxID, boxName, trayID, trayName)
        {

           for (var i =0; i &lt; myInventory.length; i++)
           {
    
     
              if (myInventory[i].getID() == siteID)
              {
                 //alert(myInventory[i].getID());
                 var tank = myInventory[i].getSubContainer(tankID);
                 if (tank != null)
                 {
                    var box = tank.getSubContainer(boxID);
                    if (box != null)
                    {
                       var tray = box.getSubContainer(trayID);
                       if (tray == null) // otherwise tray exists -- no need to add
                       {
                          var obj = new InvContainer(trayID, trayName, 'TRAY');
                          box.addSubContainer(obj);
                          return;
                       }
                        return;
                    }
                    else
                    {
                        // add both the box and the tray
                        var objBox = new InvContainer(boxID, boxName, 'BOX');
                        objBox.addSubContainer(new InvContainer(trayID, trayName, 'TRAY'));
                        tank.addSubContainer(objBox);
                        return;
                    }
                 }
                 else
                 {
                    // add the tank, box and tray
                    var objTank = new InvContainer(tankID, tankName, 'TANK');
                    var objBox = new InvContainer(boxID, boxName, 'BOX');

                    objBox.addSubContainer(new InvContainer(trayID, trayName, 'TRAY'));
                    objTank.addSubContainer(objBox);  
                    myInventory[i].addSubContainer(objTank);
                    return;
                 }
              }
          }     
             // Does not exist in set
           
          var site = new InvContainer(siteID, siteName, 'SITE');
          var tank = new InvContainer(tankID, tankName, 'TANK');
          var box = new InvContainer(boxID, boxName, 'BOX');
          var tray = new InvContainer(trayID, trayName, 'TRAY');

          box.addSubContainer(tray);
          tank.addSubContainer(box);
          site.addSubContainer(tank);
          myInventory[myInventory.length] = site;

        }
        
        // this will need to be updated
        
        
        function updateTank()
{

    var curr = document.getElementById('SITE').value;
     var element =  document.getElementById('TANK');
    
     
     
   var container = getSite(curr).subContainers;   
   
  // populate if user select site as *  
  
    if(element != null)  
    {
        resetOptions(element);
    }


      for (var i = 0; i &lt; container.length; i++)
      {
       if(container[i].getSelected() == "true")
        {
            newOpt = new Option(container[i].getName(), container[i].getID());
            element.options[i] = newOpt;
            element.options[i].selected = true;
        }
        else
        {
            var newOpt = new Option(container[i].getName(), container[i].getID())
            element.options[i] = newOpt;
        }
      }

   //  buildDropdown (myInventory[0], 'true');      
//   updateBox();


}


function resetOptions (arrayset)
{
   for (var i = 0; i &lt; arrayset.options.length; i++)
   {
      arrayset.options[i] = null;
   }
   arrayset.options.length = 0;
  
}


function getSite(siteID)
{

   for (var i = 0; i &lt; myInventory.length; i++)
   {
     if (myInventory[i].getID() == siteID)
     {
        return myInventory[i];
     }
   }

   return null;

}


function updateBox()
{
   // alert('updating yo box');
   var siteID = document.getElementById('SITE').value;
   var curr = document.getElementById('TANK').value;
   // alert('going to get site ' + siteID + ' and tank : ' + curr);
   
   var siteContainer = getSite(siteID);
   
   // if (siteContainer == null)
   //   alert ('and its null');
   
   var tankContainer = siteContainer.getSubContainer(curr);
   if (tankContainer == null) alert('tank container is null');
   
   var element =  document.getElementById('BOX');
   var boxContainer = tankContainer.subContainers;
   
   // if (boxContainer == null)
   //   alert ('box container is null');
   
   
   
   
   


   resetOptions(element);


      for (var i = 0; i &lt; boxContainer.length; i++)
      {

        var newOpt = new Option(boxContainer[i].getName(), boxContainer[i].getID())
        element.options[i] = newOpt;
        


      }

   //  buildDropdown (myInventory[0], 'true');
   updateTray();

}

function updateTray()
{
   // alert('updating trays');
   var siteID = document.getElementById('SITE').value;
   var curr = document.getElementById('TANK').value;
   var siteContainer = getSite(siteID);
   
   // if (siteContainer == null)
   //   alert ('and its null');
   
   var tankContainer = siteContainer.getSubContainer(curr);
   // if (tankContainer == null) alert('tank container is null');
   
   var boxID =  document.getElementById('BOX').value;
   var boxContainer = tankContainer.getSubContainer(boxID);
   
   // if (boxContainer == null) alert ('box container is null');
   
   var trays = boxContainer.subContainers;
   
   var element = document.getElementById('TRAY');


   resetOptions(element);


      for (var i = 0; i &lt; trays.length; i++)
      {
        
        var newOpt = new Option(trays[i].getName(), trays[i].getID())
        element.options[i] = newOpt;
 
      }

}
function setSelectedValues(SiteID,TankID)
{
    for(var i = 0;i &lt; myInventory.length;i++)
    {
        var site = myInventory[i];
        if(site.getID() == SiteID)
        {
            site.setSelected("true");
        }
        var tanks = site.subContainers;
        for(var j=0 ; j &lt; tanks.length ; j++)
        {
            var tank = tanks[j];
            if(tank.getID() == TankID)
            {
                tank.setSelected("true");            
            }
        }
    }
}
function submitform()
    {
        var site = document.getElementById('SITE').value;
        var tank = document.getElementById('TANK').value;
        var box = document.getElementById('BOX').value;

        if(site == -1000 || tank == -1000 || box.length == 0)
        {
            alert("All required fields need to be filled");
        }
        else
        {
            document.form1.submit();
        }
    }
            </script>                      
    
    <form name="form1" action="{$baseActionURL}?{$formParams}" method="post">
    <table width="100%">
        <tr valign="top">
            <td width="30%">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            Inventory hierarchy<br/><hr/>
                        </td>
                    </tr>
                    
                </table>
                
                <table width="100%">
                    <xsl:for-each select="site">
                    <!--This is the script to add the values into the inventory object-->
                        <xsl:variable name="varSiteID"><xsl:value-of select="SITE_intSiteID"/></xsl:variable>
                        <xsl:variable name="site_expanded"><xsl:value-of select="site_expanded" /></xsl:variable>
                        <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                        
                        <tr>    
                            <td width="5%">
                                <xsl:choose>
                                    <xsl:when test="$site_expanded = 'true'">
                                        <a href="{$baseActionURL}?current=view_site&amp;id={$varSiteID}&amp;target=SITE&amp;SITE_intSiteID={$varSiteID}">
                                            <img src="media/neuragenix/icons/open.gif" border="0" />
                                        </a>
                                    </xsl:when>
                                    
                                    <xsl:when test="$site_expanded = 'false'">
                                        <a href="{$baseActionURL}?current=view_site&amp;id={$varSiteID}&amp;target=SITE&amp;SITE_intSiteID={$varSiteID}">
                                            <img src="media/neuragenix/icons/closed.gif" border="0" />
                                        </a>
                                    </xsl:when>
                                </xsl:choose>
                            </td>
                            
                            <td colspan="5" width="95%" class="uportal-text">
                                <a href="{$baseActionURL}?current=view_site&amp;SITE_intSiteID={$varSiteID}">
                                    <xsl:value-of select="SITE_strSiteName" />
                                    <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                </a>
                                
                                <xsl:if test="$current_node = 'true'">
                                <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                </xsl:if>
                            </td>
                        </tr>
                        
<!--                  TANK                             -->     
                        <xsl:if test="$site_expanded='true'">
                        
                        <xsl:for-each select="tank">
                            <xsl:variable name="varTankID"><xsl:value-of select="TANK_intTankID"/></xsl:variable>
                            <xsl:variable name="usage"><xsl:value-of select="usage"/></xsl:variable>
                            <xsl:variable name="tank_expanded"><xsl:value-of select="tank_expanded" /></xsl:variable>
                            <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                            
                            <tr>    
                                <td width="5%"></td>
                                <td width="5%">
                                    <xsl:choose>
                                        <xsl:when test="$tank_expanded = 'true'">
                                            <a href="{$baseActionURL}?current=view_tank&amp;id={$varTankID}&amp;target=TANK&amp;TANK_intTankID={$varTankID}">
                                                <img src="media/neuragenix/icons/open.gif" border="0" />
                                            </a>
                                        </xsl:when>

                                        <xsl:when test="$tank_expanded = 'false'">
                                            <a href="{$baseActionURL}?current=view_tank&amp;id={$varTankID}&amp;target=TANK&amp;TANK_intTankID={$varTankID}">
                                                <img src="media/neuragenix/icons/closed.gif" border="0" />
                                            </a>
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                                <td width="5%">
                                    <xsl:choose>
                                        <xsl:when test="$usage = 0">
                                            <img src="media/neuragenix/icons/empty_tank.gif" border="0"  />
                                        </xsl:when>
                                        
                                        <xsl:when test="$usage = 1">
                                            <img src="media/neuragenix/icons/green_tank.gif" border="0"  />
                                        </xsl:when>
                                        
                                        <xsl:when test="$usage = 2">
                                            <img src="media/neuragenix/icons/yellow_tank.gif" border="0"  />
                                        </xsl:when>
                                        
                                        <xsl:when test="$usage = 3">
                                            <img src="media/neuragenix/icons/red_tank.gif" border="0"  />
                                        </xsl:when>
                                    </xsl:choose>
                                </td>

                                <td colspan="3" width="85%" class="uportal-text">
                                    <a href="{$baseActionURL}?current=view_tank&amp;TANK_intTankID={$varTankID}">
                                        <xsl:value-of select="TANK_strTankName" />
                                        <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                    </a>
                                    <xsl:if test="$current_node = 'true'">
                                    <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                    </xsl:if>
                                </td>
                            </tr>
                            
<!--                  BOX                             -->     
                            <xsl:if test="$tank_expanded='true'">                            
                            <xsl:for-each select="box">
                                <xsl:variable name="varBoxID"><xsl:value-of select="BOX_intBoxID"/></xsl:variable>
                                <xsl:variable name="usage"><xsl:value-of select="usage"/></xsl:variable>
                                <xsl:variable name="box_expanded"><xsl:value-of select="box_expanded" /></xsl:variable>
                                <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                                
                                <tr>    
                                    <td width="5%"></td>
                                    <td width="5%"></td>
                                    <td width="5%">
                                        <xsl:choose>
                                            <xsl:when test="$box_expanded = 'true'">
                                                <a href="{$baseActionURL}?current=view_box&amp;id={$varBoxID}&amp;target=BOX&amp;BOX_intBoxID={$varBoxID}">
                                                    <img src="media/neuragenix/icons/open.gif" border="0" />
                                                </a>
                                            </xsl:when>

                                            <xsl:when test="$box_expanded = 'false'">
                                                <a href="{$baseActionURL}?current=view_box&amp;id={$varBoxID}&amp;target=BOX&amp;BOX_intBoxID={$varBoxID}">
                                                    <img src="media/neuragenix/icons/closed.gif" border="0" />
                                                </a>
                                            </xsl:when>
                                        </xsl:choose>
                                    </td>
                                    
                                    <td width="5%">
                                        <xsl:choose>
                                            <xsl:when test="$usage = 0">
                                                <img src="media/neuragenix/icons/empty_tank.gif" border="0"  />
                                            </xsl:when>

                                            <xsl:when test="$usage = 1">
                                                <img src="media/neuragenix/icons/green_tank.gif" border="0"  />
                                            </xsl:when>

                                            <xsl:when test="$usage = 2">
                                                <img src="media/neuragenix/icons/yellow_tank.gif" border="0"  />
                                            </xsl:when>

                                            <xsl:when test="$usage = 3">
                                                <img src="media/neuragenix/icons/red_tank.gif" border="0"  />
                                            </xsl:when>
                                        </xsl:choose>
                                    </td>
                                
                                    <td width="80%" colspan="2" class="uportal-text">
                                        <a href="{$baseActionURL}?current=view_box&amp;BOX_intBoxID={$varBoxID}">
                                            <xsl:value-of select="BOX_strBoxName" />
                                            <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                        </a>
                                        <xsl:if test="$current_node = 'true'">
                                        <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                        </xsl:if>
                                    </td>
                                </tr>
                                
<!--                  TRAY                             -->     
                                <xsl:if test="$box_expanded='true'">   
                                <xsl:for-each select="tray">
                                    <xsl:variable name="varTrayID"><xsl:value-of select="TRAY_intTrayID"/></xsl:variable>
                                    <xsl:variable name="usage"><xsl:value-of select="usage"/></xsl:variable>  
                                    <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                                    
                                    <tr>    
                                        <td width="5%"></td>
                                        <td width="5%"></td>
                                        <td width="5%"></td>
                                        <td width="5%"></td>
                                        
                                        <td width="5%">
                                            <xsl:choose>
                                                <xsl:when test="$usage = 0">
                                                    <img src="media/neuragenix/icons/empty_tank.gif" border="0"  />
                                                </xsl:when>

                                                <xsl:when test="$usage = 1">
                                                    <img src="media/neuragenix/icons/green_tank.gif" border="0"  />
                                                </xsl:when>

                                                <xsl:when test="$usage = 2">
                                                    <img src="media/neuragenix/icons/yellow_tank.gif" border="0"  />
                                                </xsl:when>

                                                <xsl:when test="$usage = 3">
                                                    <img src="media/neuragenix/icons/red_tank.gif" border="0"  />
                                                </xsl:when>
                                            </xsl:choose>
                                        </td>

                                        <td width="75%" class="uportal-text">
                                            <a href="{$baseActionURL}?current=view_tray&amp;TRAY_intTrayID={$varTrayID}">
                                                <xsl:value-of select="TRAY_strTrayName" />
                                                <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                            </a>
                                            <xsl:if test="$current_node = 'true'">
                                            <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                            </xsl:if>
                                        </td>
                                    </tr>                     
                                </xsl:for-each>
                                </xsl:if>
                                
                            </xsl:for-each>
                            </xsl:if>
                            
                        </xsl:for-each>
                        </xsl:if>
                        
                    </xsl:for-each>
                </table>
                <br/>
                <br/>
                <br/>
                <xsl:variable name="varAdminSection"><xsl:value-of select="intAdminSection"/></xsl:variable>
                <xsl:if test="$varAdminSection=1"> 
                      <span class="uportal-channel-subtitle">
                                    Inventory administration<br/>
                                    <hr/>
                                
                       </span>
                
                
                    <table width="100%" border="0" cellspacing="0" cellpadding="2">
                       <xsl:variable name="varAdminAddSite"><xsl:value-of select="intInventoryAddSite"/></xsl:variable>
                       <xsl:variable name="varAdminAddTank"><xsl:value-of select="intInventoryAddTank"/></xsl:variable>
                       <xsl:variable name="varAdminAddBox"><xsl:value-of select="intInventoryAddBox"/></xsl:variable>
                       <xsl:variable name="varAdminAddTray"><xsl:value-of select="intInventoryAddTray"/></xsl:variable>
                      



                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddSite=1">
                                    <a href="{$baseActionURL}?current=add_site">Add <xsl:value-of select="SITE_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddTank=1">
                                    <a href="{$baseActionURL}?current=add_tank">Add <xsl:value-of select="TANK_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddBox=1">
                                    <a href="{$baseActionURL}?current=add_box">Add <xsl:value-of select="BOX_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddTray=1">
                                    <a href="{$baseActionURL}?current=add_tray">Add <xsl:value-of select="TRAY_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                       

                    </table>
                </xsl:if>

                
                
            
                
            </td>
            
            <td width="70%">
                
                <table width="100%">
                    <tr>
                        <td colspan="6" class="uportal-channel-subtitle">
                            Add New <xsl:value-of select="BOX_strTitleDisplay" /><br/><hr/>
                        </td>
                    </tr>
                   
                </table>
                
                <table width="100%">
                    <tr>
                        <td class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                </table>
                <table width="100%">
                    <!--This is the script to create dynamic drop downs-->
                    <script>
                        buildInventory("-1000", "*", "-1000","*","-1000","*", "-1000", "*");
                     </script>
                    <xsl:for-each select="InventoryList/SITE">
                        <xsl:variable name="varsiteID"><xsl:value-of select="SITE_intSiteID"/></xsl:variable>
                        <xsl:variable name="varsiteName"><xsl:value-of select="SITE_strSiteName"/></xsl:variable>
                         <script>
                          buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />',"-1000","*", "-1000","*", "-1000", "*");  
                          </script>
                          <xsl:for-each select="TANK">
                              <script>
                                    buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />', '<xsl:value-of select="TANK_intTankID" />', '<xsl:value-of select="TANK_strTankName" />', "-1000","*", "-1000", "*");           
                                </script>  
                            </xsl:for-each>
                    </xsl:for-each>
                    <xsl:if test="count(Selected) &gt; 0">
                                <script>
                                     setSelectedValues('<xsl:value-of select="Selected/SITE/SITE_intSiteID"/>','<xsl:value-of select="Selected/SITE/TANK/TANK_intTankID"/>');
                                </script>
                            </xsl:if>
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="SITE_strTitleDisplay" /> name:
                        </td>
                        <td width="25%">
                            <select id="SITE" name="SITE_intSiteID" class="uportal-input-text" onChange="updateTank()">
                         <!--<option value="-1000">*</option>-->
                      <script> 
                      <xsl:text>                 
                            for (var i = 0; i &lt; myInventory.length; i++)
                              {
                                 document.writeln('&lt;OPTION value=' + myInventory[i].getID());
                                 if(myInventory[i].getSelected() == "true")
                                 {
                                    document.writeln(' selected=true');
                                 }
                                 document.writeln('&gt;');
                                 document.writeln(myInventory[i].getName());
                                 document.writeln('&lt;/OPTION&gt;');   
                              }
                              </xsl:text>
                              
                              
                      </script>
                      </select>
                        </td>
                        <td width="5%"></td><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTitleDisplay" /> name:
                        </td>
                        <td width="30%" align="top">
                            <select id="TANK" name="BOX_intTankID" class="uportal-input-text">
                                <option selected="true" value="-1000">*</option>  
                            </select>  
                                <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <script>
                                    updateTank();
                                </script>
                                </xsl:if>
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="BOX_strBoxNameDisplay" />:
                        </td>
                        <td width="25%">
                            <xsl:choose>
                            <xsl:when test="count(/inventory/Selected) &gt; 0">
                                <input type="text" id="BOX" name="BOX_strBoxName" size="22" tabindex="2" class="uportal-input-text" >
                                <xsl:attribute name="value">
                                            <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/BOX_strBoxName"/>
                                        </xsl:attribute>
                                    </input>
                            </xsl:when>
                            <xsl:otherwise>
                                <input type="text" id="BOX" name="BOX_strBoxName" size="22" tabindex="2" class="uportal-input-text" >
                                </input>
                            </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        <td width="5%"></td><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" align="top" class="uportal-label">
                            <xsl:value-of select="BOX_strBoxDescriptionDisplay" />:
                        </td>
                        <td width="30%" align="top">
                        <xsl:choose>
                            <xsl:when test="count(/inventory/Selected) &gt; 0">
                            <textarea name="BOX_strBoxDescription" rows="4" cols="20" tabindex="3" class="uportal-input-text">                                
                                <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/BOX_strBoxDesc"/>
                            </textarea>
                            </xsl:when>
                            <xsl:otherwise>
                                <textarea name="BOX_strBoxDescription" rows="4" cols="20" tabindex="3" class="uportal-input-text">                                
                                </textarea>                            
                            </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="7"><hr /></td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr>	
                        <td width="20%">
                            <!--input type="submit" name="save" tabindex="4" value="Save" class="uportal-button" /-->
                            <input type="button" name="save" tabindex="4" value="Save" class="uportal-button" onClick="javascript:submitform();"/>
                            <input type="button" name="clear" value="Clear" tabindex="5" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_box')" />
                        </td>
                        <td width="80%"></td>
                    </tr>
                </table>
                
            </td>
        </tr>
    </table>
    </form>
    </xsl:template>

</xsl:stylesheet>
