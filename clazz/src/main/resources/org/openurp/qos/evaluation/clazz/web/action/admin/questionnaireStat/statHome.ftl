[#ftl]
[@b.head/]
[@b.toolbar title='统计评教结果']
    bar.addBack();
[/@]

[@b.form name="statForm" action="!stat"]
    <table class="infoTable" width="100%" align="center">
        <thead style="background:#C7DBFF;height:40px;line-height:40px;text-align:center;font-weight:bold;">
            <tr>
                <td colspan="9">统计评教结果</td>
            </tr>
        </thead>
        <tbody style="height:25px;line-height:25px;">
            <tr style="text-align:center;">
                <td colspan="9">
                    学年学期:[@eams.semesterCalendar title="学年学期" name="semester.id" empty="false" value=semester /]
                </td>
            </tr>
            <tr>
                <td class="title" style="text-align:center;" width="10%;">培养层次</td>
                <td style="text-align:center;" width="15%;">
                    <select name="educations" id="educations" style="width:160px;height:150px;" multiple size="10" onDblClick="">
                        [#list educations?if_exists as education]
                        <option value="${(education.id)!}">${(education.name)!}</option>
                        [/#list]
                    </select>
                </td>
                <td class="title" style="text-align:center;width:40px;">
                    <input OnClick="" type="button" value="&gt;"/>
                    <br>
                    <input OnClick="" type="button" value="&lt;"/>
                </td>
                <td style="text-align:center;" width="15%;">
                    <select name="educationSelected" id="educationSelected" style="width:160px;height:150px;" multiple size="10" onDblClick="">
                    </select>
                </td>

                <td width="10%" style="background:#EBEBEB;">&nbsp;</td>

                <td class="title" style="text-align:center;" width="10%;">部门名称</td>
                <td style="text-align:center;" width="15%;">
                    <select name="departments" id="departments" style="width:160px;height:150px;" multiple size="10" onDblClick="">
                        [#list departments?if_exists as department]
                        <option value="${(department.id)!}">${(department.name)!}</option>
                        [/#list]
                    </select>
                </td>
                <td class="title" style="text-align:center;width:40px;">
                    <input OnClick="" type="button" value="&gt;"/>
                    <br>
                    <input OnClick="" type="button" value="&lt;"/>
                </td>
                <td style="text-align:center;" width="15%;">
                    <select name="departmentSelected" id="departmentSelected" style="width:160px;height:150px;" multiple size="10" onDblClick="">
                    </select>
                </td>
            </tr>
            <tr style="background:#C7DBFF;height:40px;text-align:center;">
                <td colspan="9">
                    <input type="button" value="统计评教结果" name="btnStat" onClick="doStatistic()" class="buttonStyle" />
                </td>
            </tr>
        </tbody>
    </table>
    <input type="hidden" name="departIds" value=""/>
    <input type="hidden" name="educationIds" value=""/>
[/@]
<script type="text/javaScript">
    var statForm = document.statForm;

    function doStatistic(){
        bg.form.submit(statForm);
    }
</script>
[@b.foot/]

[#--
<#include "/template/head.ftl"/>
<BODY>
    <table id="backBar" width="100%"></table>
    <table class="frameTable_title" width="100%">
        <tr>
           <form method="post"  name="actionForm" >
                <#include "/template/time/semester.ftl"/>
        </tr>
    </table>
    <table align="center" width="100%" class="listTable">
            <input type="hidden" name="statistic" value="statistic"/>
            <input type="hidden" name="departIdSeq" value=""/>
            <input type="hidden" name="educationTypeIdSeq" value=""/>
            <input type="hidden" name="semesterIdSeq" value="${semester.id}"/>
              <tr>
                  <td class="darkColumn" colspan="2" align="center">统计评教结果</td>
              </tr>
              <tr>
                  <td  align="center" class="grayStyle" width="10%"><@text name="entity.educationType"/></td>
                  <td class="grayStyle">
                      <table>
                        <tr>
                        <#--
                            <td>
                                <select name="StdTypes" MULTIPLE size="10" style="width:200px" onDblClick="JavaScript:moveSelectedOption(this.form['StdTypes'], this.form['SelectedStdType'])">
                                    <#list calendarStdTypes as stdType>
                                        <option value="${stdType.id}">${stdType.name}</option>
                                    </#list>
                                </select>
                            </td>
                            -->
                            <td>
                                <select name="educationTypes" MULTIPLE size="10" style="width:200px" onDblClick="JavaScript:moveSelectedOption(this.form['educationTypes'], this.form['SelectedEducationLevelType'])">
                                    <#list educationTypes as educationType>
                                        <option value="${educationType.id}">${educationType.name}</option>
                                    </#list>
                                </select>
                            </td>
                               <td>
                                <input OnClick="JavaScript:moveSelectedOption(this.form['educationTypes'], this.form['SelectedEducationLevelType'])" type="button" value="&gt;">
                                <br>
                                <input OnClick="JavaScript:moveSelectedOption(this.form['SelectedEducationLevelType'], this.form['educationTypes'])" type="button" value="&lt;">
                            </td>
                            <td>
                                <select name="SelectedEducationLevelType" MULTIPLE size="10" style="width:200px;" onDblClick="JavaScript:moveSelectedOption(this.form['SelectedEducationLevelType'], this.form['educationTypes'])"></select>
                            </td>
                        </tr>
                     </table>
                 </td>
              </tr>
              <#--
              <tr>
                  <td align="center" class="grayStyle">学年度学期</td>
                  <td class="brightStyle">
                      <#if stdType?exists>${stdType.name}<#else>${calendar.studentType.name}</#if>,${calendar.year},${calendar.term}
                  </td>
              </tr>
              -->
              <tr>
                  <td align="center" class="grayStyle">部门名称</td>
                  <td class="brightStyle">
                      <table>
                          <tr>
                              <td>
                                  <select name="departments" MULTIPLE size="10" style="width:200px" onDblClick="JavaScript:moveSelectedOption(this.form['departments'], this.form['Selecteddepartments'])" >
                                    <#list departmentList?sort_by("name") as department>
                                        <option value="${department.id}">${department.name}</option>
                                    </#list>
                                </select>
                              </td>
                              <td>
                                  <input OnClick="JavaScript:moveSelectedOption(this.form['departments'], this.form['Selecteddepartments'])" type="button" value="&gt;">
                                    <br>
                                <input OnClick="JavaScript:moveSelectedOption(this.form['Selecteddepartments'], this.form['departments'])" type="button" value="&lt;">
                              </td>
                              <td>
                                  <select name="Selecteddepartments" MULTIPLE size="10" style="width:200px;" onDblClick="JavaScript:moveSelectedOption(this.form['Selecteddepartments'], this.form['departments'])">
                                 </select>
                              </td>
                          </tr>
                      </table>
                  </td>
              </tr>
              <tr>
                  <td align="center" colspan="2" class="darkColumn">
                      <input type="button" value="<@text name="field.questionnaireStatistic.statisticEvaluateResults"/>" name="button1" onClick="doStatistic(this.form)" class="buttonStyle" />
                  </td>
              </tr>
          </form>
    </table>
    <script>
        var bar = new ToolBar('backBar','统计评教结果',null,true,true);
        bar.setMessage('<@getMessage/>');

        function doStatistic(form){
            form.departIdSeq.value=getAllOptionValue(form.Selecteddepartments);
            form.educationTypeIdSeq.value=getAllOptionValue(form.SelectedEducationLevelType);
            if(""==form.educationTypeIdSeq.value){
                alert("请选择学生类别");
                return;
            }
            if(""==form.departIdSeq.value){
                alert("请选择部门");
                return
            }
            if(confirm("<@text name="field.questionnaireStatistic.statisticAffirm"/>")){
                form["button1"].disabled = true;
                form.action ="questionnaireStat.action?method=stat";
                '<font color="red" size="5"><@text name="workload.statisticInfo"/></font>';
                form.submit();
            }
        }
    </script>
</body>
<#include "/template/foot.ftl"/>
--]
