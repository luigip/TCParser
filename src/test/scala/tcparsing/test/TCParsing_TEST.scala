package tcparsing.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import tcparsing._

class TCParsing_TEST extends FunSuite with ShouldMatchers {

  test("71-1"){
    val p = TCParser.fromFile("TCHS71_1")
    val exp = """package topcoder.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import topcoder.TCHS71_1.maxLength

class TCHS71_1_TEST extends FunSuite with ShouldMatchers {

  test("Case 0") { maxLength("AGGCA") should equal (1) }

  test("Case 1") { maxLength("GGTACAGTTT") should equal (3) }

  test("Case 2") { maxLength("ACCACCAACCA") should equal (0) }

  test("Case 3") { maxLength("AAAAAAAAAAAAAAAAACCCCCCCCGGGGGGGGTTTTTTTTTTTTTTTTT") should equal (25) }

}"""
    p.composeTests should equal (exp)
  }
  test("71-2"){
    val p = TCParser.fromFile("TCHS71_2")
    val exp = """package topcoder.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import topcoder.TCHS71_2.reducedFraction

class TCHS71_2_TEST extends FunSuite with ShouldMatchers {

  test("Case 0") { reducedFraction(16, 64) should equal ("1/4") }

  test("Case 1") { reducedFraction(4784, 7475) should equal ("48/75") }

  test("Case 2") { reducedFraction(25, 25) should equal ("2/2") }

  test("Case 3") { reducedFraction(95, 19) should equal ("5/1") }

  test("Case 4") { reducedFraction(100, 1000) should equal ("1/10") }

  test("Case 5") { reducedFraction(123, 456) should equal ("123/456") }

  test("Case 6") { reducedFraction(151, 1057) should equal ("1/7") }

}"""
    p.composeTests should equal (exp)
  }
  
  test("71-3"){
    val p = TCParser.fromFile("TCHS71_3")
    val exp = """package topcoder.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import topcoder.TCHS71_3.pathOutside

class TCHS71_3_TEST extends FunSuite with ShouldMatchers {

  test("Case 0") { pathOutside(
    Array("1..0",
          "###.",
          "1...")
    ) should equal (3) }

  test("Case 1") { pathOutside(
    Array("..0..",
          ".###.",
          "..1.A")
    ) should equal (6) }

  test("Case 2") { pathOutside(
    Array("f0.F..1")
    ) should equal (7) }

  test("Case 3") { pathOutside(
    Array("0....",
          ".#B#A",
          ".#.#.",
          "b#a#1")
    ) should equal (19) }

  test("Case 4") { pathOutside(
    Array("c.0.C.C.C.1")
    ) should equal (12) }

  test("Case 5") { pathOutside(
    Array("###...",
          "#0A.1a",
          "###...")
    ) should equal (-1) }

  test("Case 6") { pathOutside(
    Array("a#c#eF.1",
          ".#.#.#..",
          ".#B#D###",
          "0....F.1",
          "C#E#A###",
          ".#.#.#..",
          "d#f#bF.1")
    ) should equal (55) }

  test("Case 7") { pathOutside(
    Array("0abcdef.FEDCBA1")
    ) should equal (14) }

}"""
    p.composeTests should equal (exp)
  }  

  test("1-1"){
    val p = TCParser.fromFile("TCHS1_1")
    val exp =
"""package topcoder.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import topcoder.TCHS1_1.averageSpeed

class TCHS1_1_TEST extends FunSuite with ShouldMatchers {

  test("Case 0") { averageSpeed(1, 50,
    Array(45, 40, 50)
    ) should be (45.0 plusOrMinus 1E-9) }

  test("Case 1") { averageSpeed(1, 50,
    Array(42,43,44,45,46,47,48,49,50,51)
    ) should be (46.0 plusOrMinus 1E-9) }

  test("Case 2") { averageSpeed(1, 50,
    Array(42,46,48,50,52)
    ) should be (0.0 plusOrMinus 1E-9) }

  test("Case 3") { averageSpeed(20, 60,
    Array(25,45,45,43,24,9,51,55,60,34,61,23,40,40,47,49,33,23,47,54,54)
    ) should be (41.68421052631579 plusOrMinus 1E-9) }

}"""
    p.composeTests should equal (exp)
  }
    
  test("1-2"){
    val p = TCParser.fromFile("TCHS1_2")
    val exp = """package topcoder.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import topcoder.TCHS1_2.language

class TCHS1_2_TEST extends FunSuite with ShouldMatchers {

  test("Case 0") { language(
    Array("a30b30c40","a20b40c40"),
    Array("aa bbbb cccc")
    ) should be (0.0 plusOrMinus 1E-9) }

  test("Case 1") { language(
    Array("a30b30c40","a20b40c40"),
    Array("aaa bbbb ccc")
    ) should be (2.0 plusOrMinus 1E-9) }

  test("Case 2") { language(
    Array("a10b10c10d10e10f50"),
    Array("abcde g")
    ) should be (10.8 plusOrMinus 1E-9) }

  test("Case 3") { language(
    Array("a09b01c03d05e20g01h01i08l06n08o06r07s09t08u07x01"
         ,"a14b02c05d06e15g01h01i07l05n07o10r08s09t05u04x01"),
    Array("this text is in english" 
         ,"the letter counts should be close to"
         ,"that in the table")
    ) should be (130.6578 plusOrMinus 1E-9) }

  test("Case 4") { language(
    Array("a09b01c03d05e20g01h01i08l06n08o06r07s09t08u07x01"
         ,"a14b02c05d06e15g01h01i07l05n07o10r08s09t05u04x01"),
    Array("en esta es una oracion en castellano"
         ,"las ocurrencias de cada letra"
         ,"deberian ser cercanas a las dadas en la tabla")
    ) should be (114.9472 plusOrMinus 1E-9) }

  test("Case 5") { language(
    Array("z99y01", "z99y01", "z99y01", "z99y01", "z99y01", 
          "z99y01", "z99y01", "z99y01", "z99y01", "z99y01"),
    Array("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    ) should be (495050.0 plusOrMinus 1E-9) }

}"""
    p.composeTests should equal (exp)
  }

  test("1-3"){
    val p = TCParser.fromFile("TCHS1_3")
    val exp = """package topcoder.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import topcoder.TCHS1_3.limeLiters

class TCHS1_3_TEST extends FunSuite with ShouldMatchers {

  test("Case 0") { limeLiters(
    Array("##..#"
         ,"#.#.#"
         ,"#.#.#"
         ,"#####")
    ) should equal (24) }

  test("Case 1") { limeLiters(
    Array("##",
          "##")
    ) should equal (8) }

  test("Case 2") { limeLiters(
    Array("######"
         ,"#....."
         ,"#.####"
         ,"#.#..#"
         ,"#.##.#"
         ,"#....#"
         ,"######")
    ) should equal (56) }

  test("Case 3") { limeLiters(
    Array("######"
         ,"#....."
         ,"#..#.."
         ,"#....."
         ,"######")
    ) should equal (36) }

  test("Case 4") { limeLiters(
    Array("#.#.#.#"
         ,".#.#.#."
         ,"#.#.#.#"
         ,".#.#.#.")
    ) should equal (36) }

}"""
    p.composeTests should equal (exp); p === 1
  }

}