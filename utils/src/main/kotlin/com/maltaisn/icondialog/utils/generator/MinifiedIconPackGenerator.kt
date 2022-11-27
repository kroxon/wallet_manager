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

package com.maltaisn.icondialog.utils.generator

import java.io.File


/**
 * Icon pack generator with methods to shrink the icon pack size.
 * Generated icon packs are not meant to be translated.
 */
abstract class MinifiedIconPackGenerator(outputDir: File, iconSize: Int)
    : IconPackGenerator(outputDir, iconSize) {

    /**
     * Shrink tag names. Most used tags are given shorter names, made of lowercase letters.
     */
    fun shrinkTags() {
        println("Shrinking tag names")

        val icons = iconPack.values.flatten()

        // Find the number of uses of each tag.
        val tagsUses = mutableMapOf<Tag, Int>()
        for (icon in icons) {
            for (tag in icon.tags) {
                tagsUses[tag] = tagsUses.getOrDefault(tag, 0) + 1
            }
        }

        // Iterate tags by decreasing number of uses and replace icon tags with shortened one.
        var i = 0
        for ((tag, _) in tagsUses.entries.sortedByDescending { it.value }) {
            // Create tag name
            var n = i + 1
            var name = ""
            do {
                name = ('a' + (n - 1) % 26) + name
                n = (n - 1) / 26
            } while (n > 0)

            // Rename tag everywhere
            val newTag = Tag(name, tag.values)
            for (icon in icons) {
                for ((j, iconTag) in icon.tags.withIndex()) {
                    if (iconTag == tag) {
                        icon.tags[j] = newTag
                        break
                    }
                }
            }

            i++
        }
    }

    /**
     * Replace groups of tags used only together with alias values.
     */
    fun createTagAliases() {
        println("Creating tag aliases")

        // Find a list of icons that uses each tag
        val tagsUses = mutableMapOf<Tag, MutableList<Icon>>()
        for (catgIcons in iconPack.values) {
            for (icon in catgIcons) {
                for (tag in icon.tags) {
                    tagsUses.getOrPut(tag) { mutableListOf() } += icon
                }
            }
        }

        // Invert map to get a map of icons with common tag groups
        val aliases = mutableMapOf<List<Icon>, MutableList<Tag>>()
        for ((tag, iconList) in tagsUses.entries) {
            if (iconList.size > 1) {
                aliases.getOrPut(iconList) { mutableListOf() } += tag
            }
        }

        // Replace common tag groups with a single tag with alias values.
        for ((iconList, tags) in aliases) {
            if (tags.size > 1) {
                val newTag = Tag(tags.joinToString("_") { it.name }, tags.flatMap { it.values })
                for (icon in iconList) {
                    icon.tags -= tags
                    icon.tags += newTag
                }
            }
        }
    }

}
