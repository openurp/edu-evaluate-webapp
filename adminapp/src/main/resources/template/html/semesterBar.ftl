<select name="semester.id">
  [#list tag.semesters as semester]<option value="${semester.id}">${semester.schoolYear} ${semester.name}</option>[/#list]
</select>