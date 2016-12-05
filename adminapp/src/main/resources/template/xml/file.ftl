[#ftl]
[#if tag.label??]
<label for="v_${tag.id}" class="title">[#if (tag.required!"")=="true"]<em class="required">*</em>[/#if]${tag.label}:</label>
[/#if]
<input type='text' id='v_${tag.id}' [#if tag.title??]title="${tag.title}"[/#if] onClick="document.getElementById('${tag.id}').click()" readOnly="readOnly" ${tag.parameterString}/>
<input type='button' onClick="document.getElementById('${tag.id}').click()" value='浏览' />
<input type="file" [#if tag.title??]title="${tag.title}"[/#if] style="display:none" name="${tag.name!('fileName')}" id="${tag.id}" onChange="document.getElementById('v_${tag.id}').value=this.value" />