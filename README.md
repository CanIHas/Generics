Can I has? True generics
========================

This will soon be renamed to Runtime Generics.

This code is terribly WIP, yet examples manage to work. Still, I'll move this to GitHub, when it will be cleaner.

Modules
-------

* model - classes for representing generic descriptors
* gathering - global AST transform that saves all generics descriptors on classpath
* gathering-example - example of gathering (duh...)
* runtime - classes that will be used to wrap creation of generic classes with proper checks
* runtime-example - terribly low-level example of runtime module, yet it works as expected (but returned generic type
    doesn't work yet)

Soon to come:
* compilation-{global/local} - ast transforms to automatize wrapping code into runtime method calls
* compilation-{global/local}-example - example for both transforms
* full-example - end-to-end example; from defining generic interface, gathering its descriptor, wrapping code using it
    into runtime classes and using it
