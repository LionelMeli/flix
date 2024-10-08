/*
 * Copyright 2023 Jakob Schneider Villumsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

mod Test.Exp.CheckedTypeCast {

    import java.io.Serializable
    import java.lang.Integer
    import java.lang.Object
    import java.lang.{StringBuilder => JStringBuilder}

    @test
    def testCheckedTypeCast01(): Object = checked_cast(null)

    @test
    def testCheckedTypeCast02(): String = checked_cast(null)

    @test
    def testCheckedTypeCast03(): Integer = checked_cast(null)

    @test
    def testCheckedTypeCast04(): Serializable = checked_cast(null)

    @test
    def testCheckedTypeCast05(): Array[String, Static] = checked_cast(null)

    @test

    def testCheckedTypeCast06(): Serializable \ IO = {
        checked_cast(new JStringBuilder())
    }

    @test
    def testCheckedTypeCast07(): Object \ IO = {
        checked_cast(new JStringBuilder())
    }

    @test
    def testCheckedTypeCast08(): Object = checked_cast("ABC")

    @test
    def testCheckedTypeCast09(): Object = region rc {
        checked_cast(Array#{} @ rc)
    }

}
