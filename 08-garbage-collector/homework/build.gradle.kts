jar {
    doFirst {
        println 'Configuring manifest...'
    }
    manifest {
        attributes(
            'Main-Class': 'ru.calculator.CalcDemo'
        )
    }
}
