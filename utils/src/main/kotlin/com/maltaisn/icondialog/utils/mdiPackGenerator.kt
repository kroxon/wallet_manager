/*
 * Copyright 2020 Nicolas Maltais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maltaisn.icondialog.utils

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.maltaisn.icondialog.utils.generator.MinifiedIconPackGenerator
import com.maltaisn.icondialog.utils.svg.PathFormatter
import com.maltaisn.icondialog.utils.svg.PathTokenizer
import java.io.File
import kotlin.random.Random


fun main(args: Array<String>) {
    val iconsJson = File(args[0])
    val catgJson = File(args[1])
    val idMapJson = File(args[2])
    val outputDir = File(args[3])

    val generator = MdiIconPackGenerator(outputDir, 2)
    generator.generate(iconsJson, catgJson, idMapJson)
}

/**
 * Class used to generate an icon pack from Material Design Icons `icons.json` and `categories.json`
 * files available at [https://github.com/PeterShaggyNoble/MDI-Sandbox/blob/master/json/icons.json].
 *
 * @param precision Number of decimals used in path data.
 */
private class MdiIconPackGenerator(
        outputDir: File, val precision: Int
) : MinifiedIconPackGenerator(outputDir, ICON_SIZE) {

    private val json = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule(
                    nullToEmptyCollection = true,
                    nullisSameAsDefault = true))

    private var mdiIcons: Map<String, MdiIcon> = emptyMap()
    private var mdiCategories: Map<String, MdiCategory> = emptyMap()

    private var idMap: MutableMap<String, Int> = mutableMapOf()

    private var remapping = false


    fun generate(iconsJson: File, catgJson: File, idMapJson: File) {
        // Parse
        println("Parsing input files")
        parseMdiFiles(iconsJson, catgJson, idMapJson)

        // Create
        println("Creating icon pack")
        val newIdMap = createIconPack()

        // Update ID map file
        json.writeValue(idMapJson, newIdMap)

        // Shrink
        createTagAliases()
        shrinkTags()

        // Export
        createFiles(true)
    }

    /** Parse input files. */
    private fun parseMdiFiles(iconsJson: File, catgJson: File, idMapJson: File) {
        mdiIcons = json.readValue(iconsJson)
        mdiCategories = json.readValue(catgJson)

        idMap.clear()
        idMap.putAll(json.readValue(idMapJson))
    }

    /**
     * Create icon pack with all icons, categories and tags.
     * Returns the new ID map.
     */
    private fun createIconPack(): Map<String, Int> {
        iconPack.clear()

        // Filter icons which aren't in specified version or icons in a blacklist.
        val icons = mdiIcons.filter { (name, icon) ->
            icon.codePoint != null && ICON_BLACKLIST.none { it.matches(name) }
                    && icon.categories.none { it in CATEGORY_BLACKLIST }
        }

        // Create categories
        var id = 0
        val categories = mutableMapOf<String, Category>()
        for ((catgName, mdiCatg) in mdiCategories) {
            if (!mdiCatg.section && catgName !in CATEGORY_BLACKLIST && catgName != OTHER_CATG) {
                val name = catgName.normalizeName()
                categories[name] = Category(id, name, mdiCatg.name)
                id++
            }
        }
        val otherCatg = Category(id, OTHER_CATG, "Other")
        categories[OTHER_CATG] = otherCatg

        // Remap IDs if needed
        if (remapping) {
            val newIdMap = mutableMapOf<String, Int>()

            // Remap old icons
            for ((_, mdiIcon) in icons) {
                val codePoint = mdiIcon.codePoint!!
                val iconId = idMap.remove(codePoint.old) ?: continue
                newIdMap[codePoint.new] = iconId
            }

            if (idMap.isNotEmpty()) {
                println("WARNING: ${idMap.size} icons have no ID equivalent after remap: " +
                        idMap.entries.joinToString { (cp, id) -> "$cp -> $id" })
            }
            idMap = newIdMap
        }

        // Create icons
        val newIdMap = mutableMapOf<String, Int>()
        val usedIds = idMap.values.toSet()

        val pathTokenizer = PathTokenizer()
        val pathFormatter = PathFormatter(precision)
        for ((iconName, mdiIcon) in icons) {
            val catg = categories[mdiIcon.categories.find { categories[it] != null }] ?: otherCatg

            // Get icon ID from map, or derive one from codepoint if not found.
            val codePoint = mdiIcon.codePoint!!
            var iconId = idMap.remove(codePoint.new)
            if (iconId == null) {
                // This is a new icon.
                iconId = codePoint.new.toInt(16)
                while (iconId in usedIds) {
                    // This ID is already in use! Use a random one.
                    iconId = Random.nextInt(0xFFFF)
                }
            }
            newIdMap[codePoint.new] = iconId!!

            // Create tags from icon name, aliases and keywords
            val tags = mutableSetOf<Tag>()
            tags += getTagsFromString(iconName)
            mdiIcon.aliases.flatMapTo(tags) { getTagsFromString(it) }
            mdiIcon.keywords.flatMapTo(tags) { getTagsFromString(it) }

            // Rewrite path data to apply precision and make sure path is compatible with Android.
            // Path formatter also produces a more compressible format.
            val pathData = pathFormatter.format(pathTokenizer.tokenize(mdiIcon.pathData))

            val icon = Icon(iconId, tags.toMutableList(), pathData, ICON_SIZE, ICON_SIZE)
            iconPack.getOrPut(catg) { mutableListOf() } += icon
        }

        if (idMap.isNotEmpty()) {
            println("WARNING: ${idMap.size} icons have been removed in new version! $idMap")
        }

        // Sort icon pack by category, with "Other" category last.
        val sortedPack = iconPack.toSortedMap()
        val otherIcons = sortedPack.remove(otherCatg)
        iconPack.clear()
        iconPack += sortedPack
        if (otherIcons != null) {
            this.iconPack[otherCatg] = otherIcons
        }

        return newIdMap
    }

    private fun getTagsFromString(str: String) = str.splitToSequence('-', '_', ' ', '&').map {
        Tag(it.normalizeName(), listOf(it.normalizeValue()))
    }.filter {
        it.name.length > 2 && "[a-z]".toRegex() in it.name
    }.toList()

    companion object {
        private const val ICON_SIZE = 24

        private val CATEGORY_BLACKLIST = listOf("logos")
        private val ICON_BLACKLIST = listOf("^.*-outline$".toRegex())

        private const val OTHER_CATG = "other"
    }

}

private data class MdiIcon(
        @JsonProperty("added") val addedIn: String = "0",
        @JsonProperty("codepoint") val codePoint: MdiCodePoint?,
        @JsonProperty("aliases") val aliases: List<String>,
        @JsonProperty("keywords") val keywords: List<String>,
        @JsonProperty("categories") val categories: List<String>,
        @JsonProperty("data") val pathData: String)

private data class MdiCategory(
        @JsonProperty("name") val name: String,
        @JsonProperty("section") val section: Boolean)

private data class MdiCodePoint(
        @JsonProperty("old") val old: String?,
        @JsonProperty("new") val new: String)
