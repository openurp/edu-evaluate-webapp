[#ftl]
[@b.head]
<link href="${base}/static/css/home.css?v2" rel="stylesheet" type="text/css" />
<script src="${base}/static/scripts/menu.js" type="text/javascript"></script>
<style>
#_menu_folder {
  height:100%;
  width:100%;
  background-color:rgba(0, 0, 0, 0.2);
  cursor:pointer;
  position:relative;
}
#_menu_folder:hover {
  height:100%;
  width:100%;
  background-color:rgba(222, 222, 222, 1);
}
.arrow-right {
        width: 0;
        height: 0;
        border-top: 6px solid transparent;
        border-bottom: 6px solid transparent;
        border-left: 6px solid rgba(0, 0, 0, 0.6);
    top:50%;
    position:absolute;
}
.arrow-left {
        width: 0;
        height: 0;
        border-top: 6px solid transparent;
        border-bottom: 6px solid transparent; 
        border-right:6px solid rgba(0, 0, 0, 0.6);
    top:50%;
    position:absolute;
}
</style>
[/@]

<nav style="margin-bottom: 0px;" role="navigation" class="navbar navbar-default">
   <div class="navbar-header">
       <img src="${school.logoUrl!}" style="width:50px;height:50px;float: left !important;"/>
       <a onclick="return bg.Go(this,null)" style="height: 20px;" class="navbar-brand" href="${base}/index.action">${(school.name)!}</a>
   </div>
   <div>
     <ul class="nav navbar-nav" style="height: 50px;" id="app_nav_bar"></ul>
     <ul class="nav navbar-nav navbar-right" style="height: 35px; padding-top: 15px;">
       <li>
        <span class="glyphicon glyphicon-user" aria-hidden="true">[@b.a href="/security/my" target="_blank" title="查看登录记录"]${user.name}&nbsp;[/@]</span>
       </li>
     <li>
     [#assign logoutUrl]index!logout?redirect=${casConfig.casServer}/logout[/#assign]
     <span class="glyphicon glyphicon-log-out" aria-hidden="true">[@b.a href=logoutUrl target="_top"]退出&nbsp;&nbsp;[/@]</span>
        </li>
      </ul>
    </div>
</nav>
<table id="mainTable" style="width:100%;">
  <tr>
     <td style="height:100%;width:10%;padding-right: 0px;" id="leftTD" valign="top">
       <div id="menu_panel" ><ul class="menu collapsible" id="menu_ul"></ul></div>
     </td>
     <td style="height:100%;width:5px">
      <div id="_menu_folder"><div id="_menu_folder_tri"></div></div>
     </td>
     <td id="rightTD" valign="top" style="padding-left: 0px;">
    <div id="main" class="ajax_container" style="padding-left: 0px; padding-right: 0px;">选择一个菜单</div>
     </td>
  </tr>
</table>

<script type="text/javascript">
  function editAccount(){
        window.open("${b.url('/security/my')}");
  }
  var menus = ${menuJson}
  var apps = ${appJson}
  var foldTemplate='<li style="margin:0px;" class="{active_class}"><a href="javascript:void(0)" class="first_menu">{menu.title}</a><ul class="acitem" style="display: none;"><div class="scroll_box" id="menu{menu.id}"></div></ul></li>'
  var menuTempalte='<li><a class="p_1" onclick="return bg.Go(this,\'main\')" href="{menu.entry}">{menu.title}</a></li>';
  var appTemplate='<li class="{active_class}"><a href="{app.url}" target="_top">{app.title}</a></li>';
  var webappBase='${webappBase}';
  function addApps(apps,jqueryElem){
    var appendHtml='';
    for(var i=0;i<apps.length;i++){
      var app = apps[i];
      [#noparse]
      appendHtml = appTemplate.replace('{app.url}',app.url.replace('${openurp.webapp}',webappBase));
      [/#noparse]
      appendHtml = appendHtml.replace('{app.title}',app.title);
      appendHtml = appendHtml.replace('{active_class}',app.name=='${thisAppName}'?"active":"");
      jqueryElem.append(appendHtml);
    }
  }
  function addMenus(menus,jqueryElem){
    var appendHtml='';
    for(var i=0;i<menus.length;i++){
      var menu = menus[i];
      if(menu.children){
        appendHtml = foldTemplate.replace('{menu.id}',menu.id);
        appendHtml = appendHtml.replace('{menu.title}',menu.title);
        appendHtml = appendHtml.replace('{active_class}',(i==0)?"expand":"");
        jqueryElem.append(appendHtml);
        addMenus(menu.children,jQuery('#menu'+menu.id));
      }else{
        appendHtml = menuTempalte.replace('{menu.id}',menu.id);
        appendHtml = appendHtml.replace('{menu.title}',menu.title);
        appendHtml = appendHtml.replace('{menu.entry}',bg.getContextPath()+menu.entry+".action");
        jqueryElem.append(appendHtml);
      }
    }
  }
  addApps(apps,jQuery('#app_nav_bar'));
  addMenus(menus,jQuery('#menu_ul'));
  
  jQuery("ul.menu li a.p_1").click(function() {
    jQuery("ul.menu li.current").removeClass('current');
    jQuery(this).parent('li').addClass('current');
  });
  jQuery(function() {
    jQuery('#_menu_folder_tri').addClass('arrow-left');
    jQuery('#_menu_folder').click(function() {
      jQuery('#leftTD').toggle(200);
      var jq_tri = jQuery('#_menu_folder_tri');
      if(jq_tri.hasClass('arrow-left')) {
        jq_tri.removeClass('arrow-left');
        jq_tri.addClass('arrow-right');
      } else {
        jq_tri.removeClass('arrow-right');
        jq_tri.addClass('arrow-left');
      }
    });
  });
  
</script>

[@b.foot/]
