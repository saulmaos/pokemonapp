package com.recodigo.pokemonapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.recodigo.pokemonapp.data.Pokemon
import com.recodigo.pokemonapp.ui.theme.PokemonAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                }
            }
        }
    }
}

@Composable
fun PokemonList(pokemonList: List<Pokemon>) {

}

@Composable
fun PokemonItemCard(pokemon: Pokemon) {

}

