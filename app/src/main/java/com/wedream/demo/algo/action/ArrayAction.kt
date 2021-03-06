package com.wedream.demo.algo.action

open class CopyAction(
    open var from: Int,
    open var to: Int
) : AlgorithmAction.ArrayAction()

class InnerCopyAction(
    override var from: Int,
    override var to: Int
) : CopyAction(from, to)

class OutSideCopyAction(
    override var from: Int,
    override var to: Int,
    var data: Array<Int> = emptyArray()
) : CopyAction(from, to)

class ImportCopyAction(
    override var from: Int,
    override var to: Int,
    var data: Array<Int> = emptyArray()
) : CopyAction(from, to)

class ExportCopyAction(
    override var from: Int,
    override var to: Int,
    var data: Array<Int> = emptyArray()
) : CopyAction(from, to)

open class PivotAction(var i: Int) : AlgorithmAction.ArrayAction()
open class SwapAction(var p1: Int, var p2: Int) : AlgorithmAction.ArrayAction()
