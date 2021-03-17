package com.recodigo.pokemonapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.LruCache
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
                Surface {
                    PokemonList(pokemonList = pokemonList())
                }
            }
        }
    }
}

@Composable
fun PokemonList(pokemonList: List<Pokemon>) {
    val cache = remember { LruCache<String, DominantColors>(12) }
    LazyColumn {
        items(pokemonList) { pokemon ->
            PokemonItemCard(pokemon = pokemon, cache = cache)
        }
    }
}

@Composable
fun PokemonItemCard(pokemon: Pokemon, cache: LruCache<String, DominantColors>) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val dominantColorState = rememberDominantColorState(
            defaultColor = Color.LightGray,
            defaultOnColor = Color.DarkGray,
            cache = cache
        )
        LaunchedEffect(key1 = pokemon.imageUrl) {
            dominantColorState.updateColorsFromImageUrl(pokemon.imageUrl)
        }
        ConstraintLayout(
            Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            dominantColorState.color.copy(0.8f),
                            dominantColorState.color
                        )
                    )
                )
                .padding(8.dp)
        ) {
            val (number, spacer, name, types, image) = createRefs()
            createVerticalChain(number, types, chainStyle = ChainStyle.SpreadInside)
            Text(
                text = "#${pokemon.number}",
                Modifier.constrainAs(number) {
                    start.linkTo(parent.start)
                },
                color = dominantColorState.onColor
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
                },
                color = dominantColorState.onColor
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
                    Modifier.weight(1f),
                    color = dominantColorState.onColor
                )
                pokemon.types.secondType?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    Chip(
                        text = it,
                        Modifier.weight(1f),
                        color = dominantColorState.onColor
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
fun Chip(text: String, modifier: Modifier = Modifier, color: Color) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = color
        )
    ) {
        Text(
            text = text,
            Modifier
                .padding(4.dp)
                .wrapContentWidth(),
            color = color
        )
    }
}