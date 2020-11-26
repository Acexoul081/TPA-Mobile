/*
 * Copyright 2019 Evstafiev Konstantin
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package edu.bluejack20_1.gogames.rawg.entity

data class Person(
    val id: Int,
    val name: String,
    val slug: String,
    val imageURL: String,
    val imageBackgroundURL: String,
    val gameCount: Int,
    val description:String,
    val reviewsCount:Int,
    val rating:String,
    val ratingTop:Int,
    val updated:String
)