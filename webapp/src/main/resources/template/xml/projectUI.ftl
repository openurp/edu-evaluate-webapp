[#ftl/]
<label for="${tag.id}">${tag.label!}:</label>
<select id="${tag.id}" name="${tag.name}"${tag.parameterString}>
    ${tag.body}
    [#if tag.empty?? && tag.empty!="false"]
    <option value="">${tag.empty}</option>
    [/#if]
    [#list tag.items as item]
        <option value="${item[tag.keyName]}"[#if tag.isSelected(item)]selected="selected"[/#if]>${item[tag.valueName]!}</option>
    [/#list]
</select>
[#if tag.semesterValue??]
[@eams.semesterCalendar label="学年学期" id="${tag.id}Semester" name="${tag.semesterName}" onChange="${tag.onSemesterChange!}" empty=tag.semesterEmpty?default("true") initCallback="${(tag.initCallback)!}" value=tag.semesterValue /]
[#else]
[@eams.semesterCalendar label="学年学期" id="${tag.id}Semester" name="${tag.semesterName}" onChange="${tag.onSemesterChange!}" empty=tag.semesterEmpty?default("true") initCallback="${(tag.initCallback)!}" /]
[/#if]
<script type="text/javascript">
    [#if !tag.value??]
        jQuery("#${tag.id}").val(jQuery.post("${base}/data-query.action",{dataType:"projectId"}));
    [/#if]
    [#if Session["projectId"]?? || projects?size<2]
        jQuery("#${tag.id}").hide();
        jQuery("#${tag.id}").prev().hide();
    [/#if]
    [#if !(tag.items[0])?exists]
        //查询project,tag.value是给定的project
        var ${tag.id}res = jQuery.post("${base}/data-query.action",{entityId:"${(tag.value)!}"},function(){
            if(${tag.id}res.status==200){
                jQuery("#${tag.id}").empty();
                if(${tag.id}res.responseText!=""){
                    [#if tag.empty?? && tag.empty!="false"]
                    jQuery("#${tag.id}").append("<option value=\"\">${tag.empty}</option>");
                    [/#if]
                    jQuery("#${tag.id}").append(${tag.id}res.responseText);
                }
            }
        },"text");
    [/#if]
    jQuery("#${tag.id}").change(function(){
        jQuery.post("${base}/data-query!changeProjectId.action",{projectId:this.value},function(){
            jQuery("#${tag.id}Semester")[0].getSemesters(jQuery("#${tag.id}Semester"),{empty:"${(tag.semesterEmpty)?default(true)?string}"[#if tag.onSemesterChange??],onChange:"${tag.onSemesterChange}"[/#if][#if (tag.semesterValue.id)??],value:"${(tag.semesterValue.id)}"[/#if]}[#if tag.initCallback??],"${tag.initCallback}"[/#if]);
            [#if tag.onChange?exists]
                eval("${tag.onChange}");
            [/#if]
        },"text");
    });
</script>
