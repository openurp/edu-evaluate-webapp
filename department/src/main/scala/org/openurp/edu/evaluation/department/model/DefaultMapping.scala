package org.openurp.edu.evaluation.department.model

import scala.reflect.runtime.universe
import org.beangle.data.model.bind.Mapping

/**
 * @author xinzhou
 */
class DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("date")

    //lesson result
    bind[DepartEvaluate].on(e => declare(
      e.department & e.questionnaire & e.evaluateAt are notnull,
      e.questionResults is depends("evaluateResult"),
      e.remark is length(20)))

    bind[DepartQuestion].on(e => declare(
      e.result & e.questionType & e.question & e.score are notnull))

    bind[EvaluateSwitch].on(e => declare(
      e.endOn & e.beginOn & e.opened & e.semester & e.questionnaire are notnull))

  }

}
