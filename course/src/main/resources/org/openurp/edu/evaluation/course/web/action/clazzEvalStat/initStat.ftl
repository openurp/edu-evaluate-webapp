    <table class="frameTable" width="100%" cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="2"><table id="initStatBar" width="100%"></table></td>
        </tr>
        <tr valign="top">
            <td width="50%">
            <input type="hidden" name="evaluateResult.semester.id" value="${semester.id}"/>
            <input type="hidden" name="searchFormFlag" value="${Parameters["searchFormFlag"]?if_exists}"/>
        </form>
                <table id="educationTypeBar" width="100%"></table>
                <@table.table id="educationType" width="100%">
                    <@table.thead>
                        <@table.selectAllTd id="educationTypeId"/>
                        <@table.td name="attr.code"/>
                        <@table.td name="attr.name"/>
                    </@>
                    <@table.tbody datas=educationTypes;educationType>
                        <@table.selectTd id="educationTypeId" value=educationType.id/>
                        <td>${educationType.code}</td>
                        <td><@i18nName educationType/></td>
                    </@>
                </@>
                <br><br><br>
                <#--
                <center>
                <button onclick="stat()">统计</button>
                </center>
                -->
            </td>
            <td>
                <table id="departmentBar" width="100%"></table>
                <@table.table id="department" width="100%">
                    <@table.thead>
                        <@table.selectAllTd id="departmentId"/>
                        <@table.td name="attr.code"/>
                        <@table.td name="attr.name"/>
                    </@>
                    <@table.tbody datas=departments;department>
                        <@table.selectTd id="departmentId" value=department.id/>
                        <td>${department.code}</td>
                        <td><@i18nName department/></td>
                    </@>
                </@>
            </td>
        </tr>
    </table>
