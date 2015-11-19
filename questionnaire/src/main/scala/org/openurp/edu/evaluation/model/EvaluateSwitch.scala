package org.openurp.edu.evaluation.model

import org.beangle.data.model.TemporalOn
import org.beangle.data.model.IntId
import org.openurp.base.model.Semester

class EvaluateSwitch extends IntId with TemporalOn {

  var semester:Semester = _
  var isOpen:Boolean= false
  
}

