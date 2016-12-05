[#ftl]
<div [#if tag.divId??]id="${tag.divId}"[/#if] style="background: url('${base}/static/images/semesterBarBg.png') repeat-x scroll 50% 50% #DEEDF7;border: 1px solid #AED0EA;color: #222222;font-weight: bold;height:28px;">

[@b.form name=tag.formName!('semesterForm') action=tag.action! target=tag.target! theme="xml"]
<div style="margin-left:4px;margin-top:2px;float:left;line-height:22px;height:22px;">
    ${tag.body}
</div>
<div style="margin-left:10px;margin-top:2px;float:left;line-height:18px;height:22px;">
[@eams.projectUI label=tag.label!(b.text("entity.project")) onChange=tag.onChange onSemesterChange=tag.onSemesterChange empty=tag.empty semesterEmpty=tag.semesterEmpty id=tag.id name=tag.name semesterName=tag.semesterName  value=tag.value semesterValue=tag.semesterValue initCallback=tag.initCallback/]
[#if !tag.submit??]
[@b.submit value="${(tag.submitValue)!('切换学期')}"/]
[#else]
<input type="submit" value="${(tag.submitValue)!('切换学期')}" onclick="${tag.submit};return false;"/>
[/#if]
</div>
[/@]
</div>