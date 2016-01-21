[#ftl]
[@b.head/]
<div class="grid">
    [@b.messages slash="4"/]
    [@b.form name="evaluateForm" action="!loadTextEvaluate" target="contentDiv"]
    <table class="gridtable">
        <thead class="gridhead">
            <tr>
                <th width="12%">课程序号</th>
                <th width="12%">课程代码</th>
                <th>课程名称</th>
                <th>开课院系</th>
                <th>教师姓名</th>
                <th width="6%">是否评教</th>
                <th width="10%">操作方式</th>
            </tr>
        </thead>
        [#assign k = 0/]
        [#if lessons??]
        <tbody>
        [#list lessons?sort_by("no") as lesson]
        [#if lesson_index % 2 == 0]
            [#assign lessonClass="griddata-even"/]
        [#else]
            [#assign lessonClass="griddata-odd"/]
        [/#if]
            [#list lesson.teachers?if_exists as teacher]
            [#assign k = k+1/]
            [#if "1" == evaluateMap[lesson.id?string + "_" + teacher.id?string]?default("0")]
                [#assign flag = true/]
            [#else]
                [#assign flag = false]
            [/#if]
            <tr class="${lessonClass!}">
                <td>${(lesson.no)!}</td>
                <td>${(lesson.course.code)!}</td>
                <td>${(lesson.course.name)!}</td>
                <td>${(lesson.teachDepart.name)!}</td>
                <td>${(teacher.person.name.formatedName)!}</td>
                <td>[#if flag]已评教[#else]未评教[/#if]</td>
                <td>
                    <a href="javascript:doEvaluate('${flag?string("update","evaluate")}','${(lesson.id)!},${(teacher.id)!}')">
                    [#if flag]修改结果[#else]进行评估[/#if]
                    </a>
                </td>
            </tr>
            [/#list]
        [/#list]
        [#if k > 0]
        <tr class="darkColumn">
            <td colspan="6" height="30px;" align="center">
                <input type="button" class="buttonStyle" value="查看评教回复及教师公告" onClick="showRemessage();">
            </td>
        </tr>
        [/#if]
        </tbody>
        [/#if]
    </table>
    <input type="hidden" name="lesson.ids" value="[#list lessons?if_exists as lesson]${(lesson.id)!}[#if lesson_has_next],[/#if][/#list]"/>
    [/@]
    [#if !lessons??]
    <div class="gridempty" style="height: 112px;">
        <div style="padding-top: 40px;">没有查询结果</div>
    </div>
    [/#if]
</div>
<script type="text/javaScript">
    function doEvaluate(value,id){
        var evaluateForm = document.evaluateForm;
        bg.form.addInput(evaluateForm, "evaluateState", value);
        bg.form.addInput(evaluateForm, "evaluateId" ,id);
        bg.form.submit(evaluateForm);
    }
    function showRemessage(){
        var evaluateForm = document.evaluateForm;
        bg.form.submit(evaluateForm, "${b.url('!remsgList')}");
    }
</script>
[@b.foot/]

