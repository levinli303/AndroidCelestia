/*
 * SettingsModel.kt
 *
 * Copyright (C) 2001-2020, the Celestia Development Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package space.celestia.mobilecelestia.settings

import space.celestia.mobilecelestia.common.CommonSectionV2
import space.celestia.mobilecelestia.common.RecyclerViewItem
import space.celestia.mobilecelestia.common.TitledFragment
import space.celestia.mobilecelestia.utils.CelestiaString
import space.celestia.mobilecelestia.utils.PreferenceManager
import java.io.Serializable

enum class SettingsKey(private val rawDisplayName: String) : PreferenceManager.Key, Serializable {
    // Boolean values
    ShowStars("Stars"),
    ShowPlanets("Planets"),
    ShowDwarfPlanets("Dwarf Planets"),
    ShowMoons("Moons"),
    ShowMinorMoons("Minor Moons"),
    ShowAsteroids("Asteroids"),
    ShowComets("Comets"),
    ShowSpacecrafts("Spacecrafts"),
    ShowGalaxies("Galaxies"),
    ShowNebulae("Nebulae"),
    ShowGlobulars("Globulars"),
    ShowOpenClusters("Open Clusters"),
    ShowAtmospheres("Atmospheres"),
    ShowCloudMaps("Clouds"),
    ShowCloudShadows("Cloud Shadows"),
    ShowNightMaps("Night Lights"),
    ShowPlanetRings("Planet Rings"),
    ShowRingShadows("Ring Shadows"),
    ShowCometTails("Comet Tails"),
    ShowEclipseShadows("Eclipse Shadows"),
    ShowOrbits("Orbits"),
    ShowStellarOrbits("Stars"),
    ShowPlanetOrbits("Planets"),
    ShowDwarfPlanetOrbits("Dwarf Planets"),
    ShowMoonOrbits("Moons"),
    ShowMinorMoonOrbits("Minor Moons"),
    ShowAsteroidOrbits("Asteroids"),
    ShowCometOrbits("Comets"),
    ShowSpacecraftOrbits("Spacecrafts"),
    ShowCelestialSphere("Equatorial"),
    ShowEclipticGrid("Ecliptic"),
    ShowHorizonGrid("Horizontal"),
    ShowGalacticGrid("Galactic"),
    ShowDiagrams("Constellations"),
    ShowConstellationLabels("Constellation Labels"),
    ShowLatinConstellationLabels("Constellations in Latin"),
    ShowBoundaries("Show Boundaries"),
    ShowStarLabels("Stars"),
    ShowPlanetLabels("Planets"),
    ShowDwarfPlanetLabels("Dwarf Planets"),
    ShowMoonLabels("Moons"),
    ShowMinorMoonLabels("Minor Moons"),
    ShowAsteroidLabels("Asteroids"),
    ShowCometLabels("Comets"),
    ShowSpacecraftLabels("Spacecrafts"),
    ShowGalaxyLabels("Galaxies"),
    ShowNebulaLabels("Nebulae"),
    ShowGlobularLabels("Globulars"),
    ShowOpenClusterLabels("Open Clusters"),
    ShowLocationLabels("Locations"),
    ShowCityLabels("Cities"),
    ShowObservatoryLabels("Observatories"),
    ShowLandingSiteLabels("Landing Sites"),
    ShowMonsLabels("Mons"),
    ShowMareLabels("Mare"),
    ShowCraterLabels("Crater"),
    ShowVallisLabels("Vallis"),
    ShowTerraLabels("Terra"),
    ShowEruptiveCenterLabels("Volcanoes"),
    // Int values
    TimeZone("Time Zone"),
    DateFormat("Date Format"),
    Resolution("Texture Resolution"),
    StarStyle("Star Style"),
    HudDetail("Info Display"),
    // Double values
    FaintestVisible("Faintest Visible"),
    AmbientLightLevel("Ambient Light Level"),
    GalaxyBrightness("Galaxy Brightness"),
    MinimumFeatureSize("Minimum FeatureSize");

    val displayName: String
        get() = CelestiaString(rawDisplayName, "")

    companion object {
        val allBooleanCases: List<SettingsKey>
            get() = listOf(
                // Boolean values
                ShowStars,
                ShowPlanets,
                ShowDwarfPlanets,
                ShowMoons,
                ShowMinorMoons,
                ShowAsteroids,
                ShowComets,
                ShowSpacecrafts,
                ShowGalaxies,
                ShowNebulae,
                ShowGlobulars,
                ShowOpenClusters,
                ShowAtmospheres,
                ShowCloudMaps,
                ShowCloudShadows,
                ShowNightMaps,
                ShowPlanetRings,
                ShowRingShadows,
                ShowCometTails,
                ShowEclipseShadows,
                ShowOrbits,
                ShowStellarOrbits,
                ShowPlanetOrbits,
                ShowDwarfPlanetOrbits,
                ShowMoonOrbits,
                ShowMinorMoonOrbits,
                ShowAsteroidOrbits,
                ShowCometOrbits,
                ShowSpacecraftOrbits,
                ShowCelestialSphere,
                ShowEclipticGrid,
                ShowHorizonGrid,
                ShowGalacticGrid,
                ShowDiagrams,
                ShowConstellationLabels,
                ShowLatinConstellationLabels,
                ShowBoundaries,
                ShowStarLabels,
                ShowPlanetLabels,
                ShowDwarfPlanetLabels,
                ShowMoonLabels,
                ShowMinorMoonLabels,
                ShowAsteroidLabels,
                ShowCometLabels,
                ShowSpacecraftLabels,
                ShowGalaxyLabels,
                ShowNebulaLabels,
                ShowGlobularLabels,
                ShowOpenClusterLabels,
                ShowLocationLabels,
                ShowCityLabels,
                ShowObservatoryLabels,
                ShowLandingSiteLabels,
                ShowMonsLabels,
                ShowMareLabels,
                ShowCraterLabels,
                ShowVallisLabels,
                ShowTerraLabels,
                ShowEruptiveCenterLabels
            )

        val allIntCases: List<SettingsKey>
            get() = listOf(
                TimeZone,
                DateFormat,
                Resolution,
                StarStyle,
                HudDetail
            )

        val allDoubleCases: List<SettingsKey>
            get() = listOf(
                FaintestVisible,
                AmbientLightLevel,
                GalaxyBrightness,
                MinimumFeatureSize
            )
    }

    override val valueString: String
        get() = toString()
}

interface SettingsItem : RecyclerViewItem {
    val name: String
}

class SettingsMultiSelectionItem(
    private val rawDisplayName: String,
    val masterKey: String?,
    val selections: List<Selection>
) : SettingsItem, Serializable {
    override val name: String
        get() = CelestiaString(rawDisplayName, "")

    constructor(rawDisplayName: String, selections: List<Selection>) : this(rawDisplayName, null, selections)

    constructor(internalKey: SettingsKey, selections: List<Selection>) : this(internalKey.displayName, internalKey.valueString, selections)

    class Selection(internalKey: SettingsKey) : RecyclerViewItem, Serializable {
        val name: String = internalKey.displayName
        val key: String = internalKey.valueString
    }
}

private val staticDisplayItems: List<SettingsMultiSelectionItem> = listOf(
    SettingsMultiSelectionItem("Objects", listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowStars),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowPlanets),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowDwarfPlanets),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMoons),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMinorMoons),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowAsteroids),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowComets),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowSpacecrafts),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowGalaxies),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowNebulae),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowGlobulars),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowOpenClusters)
    )),
    SettingsMultiSelectionItem("Features", listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowAtmospheres),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCloudMaps),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCloudShadows),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowNightMaps),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowPlanetRings),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowRingShadows),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCometTails),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowEclipseShadows)
    )),
    SettingsMultiSelectionItem(SettingsKey.ShowOrbits, listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowStellarOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowPlanetOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowDwarfPlanetOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMoonOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMinorMoonOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowAsteroidOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCometOrbits),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowSpacecraftOrbits)
    )),
    SettingsMultiSelectionItem("Grids", listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCelestialSphere),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowEclipticGrid),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowHorizonGrid),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowGalacticGrid)
    )),
    SettingsMultiSelectionItem(SettingsKey.ShowDiagrams, listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowConstellationLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowLatinConstellationLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowBoundaries)
    )),
    SettingsMultiSelectionItem("Object Labels", listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowStarLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowPlanetLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowDwarfPlanetLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMoonLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMinorMoonLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowAsteroidLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCometLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowSpacecraftLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowGalaxyLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowNebulaLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowGlobularLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowOpenClusterLabels)
    )),
    SettingsMultiSelectionItem(SettingsKey.ShowLocationLabels, listOf(
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCityLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowObservatoryLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowLandingSiteLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMonsLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowMareLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowCraterLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowVallisLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowTerraLabels),
        SettingsMultiSelectionItem.Selection(SettingsKey.ShowEruptiveCenterLabels)
    ))
)

class SettingsSingleSelectionItem(
    private val internalKey: SettingsKey,
    val selections: List<Selection>) : SettingsItem, Serializable {
    class Selection(private val rawDisplayName: String, val value: Int) : RecyclerViewItem, Serializable {
        val name: String
            get() = CelestiaString(rawDisplayName, "")
    }

    val key = internalKey.valueString

    override val name: String
        get() = internalKey.displayName
}

class SettingsCurrentTimeItem : SettingsItem {
    override val name: String
        get() = CelestiaString("Current Time", "")
}

private val staticTimeItems: List<SettingsItem> = listOf(
    SettingsSingleSelectionItem(SettingsKey.TimeZone, listOf(
        SettingsSingleSelectionItem.Selection("Local Time", 0),
        SettingsSingleSelectionItem.Selection("UTC", 1)
    )),
    SettingsSingleSelectionItem(SettingsKey.DateFormat, listOf(
        SettingsSingleSelectionItem.Selection("Default", 0),
        SettingsSingleSelectionItem.Selection("YYYY MMM DD HH:MM:SS TZ", 1),
        SettingsSingleSelectionItem.Selection("UTC Offset", 2)
    )),
    SettingsCurrentTimeItem()
)

class SettingsDataLocationItem : SettingsItem {
    override val name: String
        get() = CelestiaString("Data Location", "")
}

private val staticAdvancedItems: List<SettingsItem> = listOf(
    SettingsSingleSelectionItem(SettingsKey.Resolution, listOf(
        SettingsSingleSelectionItem.Selection("Low", 0),
        SettingsSingleSelectionItem.Selection("Medium", 1),
        SettingsSingleSelectionItem.Selection("High", 2)
    )),
    SettingsSingleSelectionItem(SettingsKey.StarStyle, listOf(
        SettingsSingleSelectionItem.Selection("Fuzzy Points", 0),
        SettingsSingleSelectionItem.Selection("Points", 1),
        SettingsSingleSelectionItem.Selection("Scaled Discs", 2)
    )),
    SettingsSingleSelectionItem(SettingsKey.HudDetail, listOf(
        SettingsSingleSelectionItem.Selection("None", 0),
        SettingsSingleSelectionItem.Selection("Terse", 1),
        SettingsSingleSelectionItem.Selection("Verbose", 2)
    )),
    SettingsDataLocationItem()
)

class SettingsRenderInfoItem : SettingsItem {
    override val name: String
        get() = CelestiaString("Render Info", "")
}

class SettingsAboutItem : SettingsItem {
    override val name: String
        get() = CelestiaString("About", "")
}

private val staticOtherItems: List<SettingsItem> = listOf(
    SettingsRenderInfoItem(),
    SettingsAboutItem()
)

val mainSettingSections: List<CommonSectionV2> = listOf(
    CommonSectionV2(staticDisplayItems, CelestiaString("Display", "")),
    CommonSectionV2(staticTimeItems, CelestiaString("Time", "")),
    CommonSectionV2(staticAdvancedItems, CelestiaString("Advanced", "")),
    CommonSectionV2(staticOtherItems, CelestiaString("Other", ""))
)

open class SettingsBaseFragment: TitledFragment() {
    open fun reload() {}
}