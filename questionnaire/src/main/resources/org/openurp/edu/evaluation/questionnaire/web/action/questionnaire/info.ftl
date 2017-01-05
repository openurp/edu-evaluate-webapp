[#ftl]
[@b.head/]
[@b.toolbar title="问卷详细信息"]
    bar.addBack();
[/@]
[#if questionnaire.title?exists]
<pre align="center" ><b>
${(questionnaire.title?html)?default('')}</b>
</pre>
[/#if]
<div align="center" style="font:bold 15px/20px">问卷描述：${(questionnaire.description?html)?default('')}&nbsp;&nbsp;&nbsp;问卷制作部门:${questionnaire.depart.name}&nbsp;&nbsp;&nbsp;</div>
<div class="grid">
    <table class="gridtable" >
        <thead class="gridhead">
            <tr>
                <th width="15%">问题类型</th>
                <th width="60%">问题内容</th>
                <th width="25">选项</th>
            </tr>
        </thead>
        <tbody>
            [#assign index=1]
            [#list questionTree?keys?sort_by("priority")?reverse as key]
                <tr class="griddata-${(key_index%2==0)?string("even","odd")}">
                    <td rowSpan="${questionTree.get(key)?size}" align="center">
                        ${key.name}
                    </td>
                    <td align="left">
                        ${index}:${questionTree.get(key)[0].content}
                    </td>
                    <td align="center">
                        [#if (questionTree.get(key)[0].optionGroup.options)?exists]
                            [#list (questionTree.get(key)[0].optionGroup.options)?sort_by("proportion")?reverse as option]
                                 <input type="radio" name="select${questionTree.get(key)[0].id}" value="${option.id}">${option.name}&nbsp;
                            [/#list]
                        [/#if]
                    </td>
                </tr>
                [#if questionTree.get(key)?size>1]
                    [#list 1..questionTree.get(key)?size-1 as i]
                        [#assign index=index+1]
                        <tr class="griddata-${(key_index%2==0)?string("even","odd")}"> 
                            <td align="left">
                                ${index}:${questionTree.get(key)[i].content}(${questionTree.get(key)[i].priority})
                            </td>
                            <td align="center">
                                [#if (questionTree.get(key)[i].optionGroup.options)?exists]
                                    [#list (questionTree.get(key)[i].optionGroup.options)?sort_by("proportion")?reverse as option]
                                         <input type="radio" name="select${questionTree.get(key)[i].id}" value="${option.id}">${option.name}&nbsp;
                                    [/#list]
                                [/#if]
                            </td>
                        </tr>
                    [/#list]
                [/#if]
                [#assign index=index+1]
            [/#list]
            <tr style="background-color: #c7dbff;">
                <td colSpan="3">&nbsp;</td>
               </tr>
        </tbody>
    </table>
    <br>    
    [#--[#if oppositeQuestions?size >0]
    [@b.grid items=oppositeQuestions var="oppositeQuestion"]
    [@b.row]
        [@b.col property="orginQuestion.content" title="原始问题" width="50%"]${(oppositeQuestion.orginQuestion.content?html)!}[/@]
        [@b.col property="oppoQuestion.content" title="对立问题" width="50%"]${(oppositeQuestion.oppoQuestion.content?html)!}[/@]
        
    [/@]
    [/@]
    [/#if]--]
</div>
[@b.foot/]