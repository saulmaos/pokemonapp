package com.recodigo.pokemonapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.recodigo.pokemonapp.data.Pokemon
import com.recodigo.pokemonapp.data.pokemonList
import com.recodigo.pokemonapp.ui.theme.PokemonAppTheme
import dev.chrisbanes.accompanist.coil.CoilImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonAppTheme {
                Surface(color = Color.Red) {
                    PokemonList(pokemonList = pokemonList())
                }
            }
        }
    }
}

@Composable
fun PokemonList(pokemonList: List<Pokemon>) {
    LazyColumn {
        items(pokemonList) { pokemon ->
            PokemonItemCard(pokemon = pokemon)
        }
    }
}

@Composable
fun PokemonItemCard(pokemon: Pokemon) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ConstraintLayout(Modifier.padding(8.dp).background(Color.LightGray)) {
            val (number, spacer, name, types, image) = createRefs()
            createVerticalChain(number, types, chainStyle = ChainStyle.SpreadInside)
            Text(
                text = "#${pokemon.number}",
                Modifier.constrainAs(number) {
                    start.linkTo(parent.start)
                }
            )
            Spacer(modifier = Modifier
                .width(8.dp)
                .constrainAs(spacer) {
                    start.linkTo(number.end)
                })
            Text(
                text = pokemon.name,
                Modifier.constrainAs(name) {
                    top.linkTo(number.top)
                    start.linkTo(spacer.end)
                    end.linkTo(image.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
            )
            Row(
                Modifier.constrainAs(types) {
                    start.linkTo(parent.start)
                    end.linkTo(image.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
            ) {
                Chip(
                    text = pokemon.types.firstType,
                    Modifier.weight(1f)
                )
                pokemon.types.secondType?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    Chip(
                        text = it,
                        Modifier.weight(1f)
                    )
                }
            }
            CoilImage(
                data = pokemon.imageUrl,
                contentDescription = "pokemon",
                modifier = Modifier
                    .size(70.dp)
                    .constrainAs(image) {
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun Chip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.onSurface
        )
    ) {
        Text(
            text = text,
            Modifier
                .padding(4.dp)
                .wrapContentWidth()
        )
    }
}