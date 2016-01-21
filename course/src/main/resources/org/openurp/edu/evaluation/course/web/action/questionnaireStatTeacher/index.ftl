[#ftl]
[@b.head/]
[@b.toolbar title='我的评教情况' id='textEvaluationBar'/]
    
    [@eams.semesterBar name="project.id" semesterEmpty=false semesterName="semester.id" semesterValue=semester/]
      <table width="100%" class="frameTable">
        <tr>
            <td class="index_content">
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv"]
                <input type="hidden" name="semester.id" value="${(semester.id)!}"/>
            <input type="hidden" name="questionnaireStat.teacher.id" value="${teacher.id}">
            [/@]
            [@b.div href="!search" id="contentDiv"/]
        </td> 
        </tr>    
    </table>
[@b.foot/]