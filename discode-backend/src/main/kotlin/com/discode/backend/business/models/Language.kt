package com.discode.backend.business.models

enum class Language(val id: String, val compiler: String) {
    GO("go", "gccgo102"),
    C("c", "cclang1300"),
    FORTRAN("fortran", "gfortran101"),
    CXX("c++", "clang1300"),
    PYTHON("python", "python310"),
    RUBY("ruby", "ruby302"),
    D("d", "gdc101"),
    RUST("rust", "r190"),
    JAVA("java", "java1700"),
    KOTLIN("kotlin", "kotlinc1610"),
    PASCAL("pascal", "fpc320"),
    HASKELL("haskell", "ghc921"),
    SWIFT("swift", "swift55"),
    ZIG("zig", "ztrunk")
}