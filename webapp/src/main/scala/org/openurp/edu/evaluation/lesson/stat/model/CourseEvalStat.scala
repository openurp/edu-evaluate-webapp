package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.data.model.LongId
import org.openurp.edu.base.model.Course
import org.openurp.edu.base.model.Teacher

class CourseEvalStat extends LongId with EvalStat with Rank {

  var teacher: Teacher = _
  var course: Course = _

}

class CourseOptionStat extends LongId with OptionStat

class CourseQuestionStat extends LongId with QuestionStat

class CourseQuestionTypeStat extends LongId with QuestionTypeStat
