package com.recodigo.pokemonapp.data

data class Pokemon(
    val number: String,
    val name: String,
    val types: PokemonTypes,
    val imageUrl: String = pokemonUrl(number)
)

data class PokemonTypes(
    val firstType: String,
    val secondType: String? = null
)

fun pokemonUrl(number: String) =
    "https://pokeres.bastionbot.org/images/pokemon/$number.png"

fun pokemonList() =
    listOf(
        Pokemon("1","Bulbasaur", PokemonTypes("Grass", "Poison")),
        Pokemon("2","Ivysaur", PokemonTypes("Grass", "Poison")),
        Pokemon("3","Venusaur", PokemonTypes("Grass", "Poison")),
        Pokemon("4","Charmander", PokemonTypes("Fire")),
        Pokemon("5","Charmeleon", PokemonTypes("Fire")),
        Pokemon("6","Charizard", PokemonTypes("Fire", "Flying")),
        Pokemon("7","Squirtle", PokemonTypes("Water")),
        Pokemon("8","Wartortle", PokemonTypes("Water")),
        Pokemon("9","Blastoise", PokemonTypes("Water")),
        Pokemon("10","Caterpie", PokemonTypes("Bug")),
        Pokemon("11","Metapod", PokemonTypes("Bug")),
        Pokemon("12","Butterfree", PokemonTypes("Bug", "Flying"))
    )
