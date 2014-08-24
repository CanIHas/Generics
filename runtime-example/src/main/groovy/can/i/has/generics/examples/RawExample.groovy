package can.i.has.generics.examples

import can.i.has.generics.runtime.GenericsRuntime

def listOfStrings = GenericsRuntime.makeMeGeneric(["a", "b", "c"], E: String)

listOfStrings.add "XYZ"
try {
    listOfStrings.add 3.14
} catch (AssertionError ignoredAndWillBeOfOtherType) {}