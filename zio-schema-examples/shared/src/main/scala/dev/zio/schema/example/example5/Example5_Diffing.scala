package dev.zio.schema.example.example5

import zio._
import zio.schema.Schema._
import zio.schema._

/**
 * Example 5: In this example, we use ZIO-Schema to detect changes in our objects.
 *
 */
private[example5] object Domain {
  final case class Person(name: String, age: Int)

  object Person {

    val name: Field[Person, String] =
      Field[Person, String]("name", primitive[String], get = _.name, set = (p, v) => p.copy(name = v))

    val age: Field[Person, Int] =
      Field[Person, Int]("age", primitive[Int], get = _.age, set = (p, v) => p.copy(age = v))

    val schema: Schema[Person] = CaseClass2[String, Int, Person](
      TypeId.parse("dev.zio.schema.example.example5.Domain.Person"),
      field1 = name,
      field2 = age,
      construct = (name, age) => Person(name, age)
    )
  }

  final case class PersonDTO(firstname: String, lastname: String, years: Int)

  object PersonDTO {

    val firstname: Field[PersonDTO, String] =
      Field("firstname", primitive[String], get = _.firstname, set = (p, v) => p.copy(firstname = v))

    val lastname: Field[PersonDTO, String] =
      Field("lastname", primitive[String], get = _.lastname, set = (p, v) => p.copy(lastname = v))
    val years: Field[PersonDTO, Int] = Field("years", primitive[Int], get = _.years, set = (p, v) => p.copy(years = v))

    val schema: Schema[PersonDTO] = CaseClass3[String, String, Int, PersonDTO](
      TypeId.parse("dev.zio.schema.example.example5.Domain.PersonDTO"),
      field1 = firstname,
      field2 = lastname,
      field3 = years,
      construct = (fn, ln, y) => PersonDTO(fn, ln, y)
    )
  }

}

object Example5_Diffing extends ZIOAppDefault {

  import Domain._

  val personDTO: PersonDTO = PersonDTO("Mike", "Moe", 32)

  val diff: Patch[PersonDTO] = PersonDTO.schema.diff(personDTO, personDTO.copy(lastname = "Max"))

  override val run: UIO[Unit] = ZIO.debug(diff)
}
