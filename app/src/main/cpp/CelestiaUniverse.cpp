#include "CelestiaJNI.h"
#include <celengine/universe.h>
#include <celutil/gettext.h>

#include <map>
#include <vector>
#include <string>

#include <json.hpp>

extern "C"
JNIEXPORT jlong JNICALL
Java_space_celestia_mobilecelestia_core_CelestiaUniverse_c_1getStarCatalog(JNIEnv *env,
                                                                           jobject thiz) {
    Universe *u = (Universe *)env->GetLongField(thiz, cunPtrFieldID);
    return (jlong)u->getStarCatalog();
}

extern "C"
JNIEXPORT jlong JNICALL
Java_space_celestia_mobilecelestia_core_CelestiaUniverse_c_1getDSOCatalog(JNIEnv *env,
                                                                          jobject thiz) {
    Universe *u = (Universe *)env->GetLongField(thiz, cunPtrFieldID);
    return (jlong)u->getDSOCatalog();
}

extern "C"
JNIEXPORT jlong JNICALL
Java_space_celestia_mobilecelestia_core_CelestiaUniverse_c_1findObject(JNIEnv *env, jobject thiz,
                                                                       jstring name) {
    Universe *u = (Universe *)env->GetLongField(thiz, cunPtrFieldID);
    const char *str = env->GetStringUTFChars(name, nullptr);
    Selection *sel = new Selection(u->find(str));
    env->ReleaseStringUTFChars(name, str);
    return (jlong)sel;
}

using namespace std;

using json = nlohmann::json;

const static std::string BROWSER_ITEM_NAME_KEY = "name";
const static std::string BROWSER_ITEM_TYPE_KEY = "type";
const static jint BROWSER_ITEM_TYPE_BODY = 0;
const static jint BROWSER_ITEM_TYPE_LOCATION = 1;
const static std::string BROWSER_ITEM_POINTER_KEY = "pointer";
const static std::string BROWSER_ITEM_CHILDREN_KEY = "children";

static json create_browser_item(std::string name, jint key, map<string, pair<jlong, string>> mp) {
    json j;
    j[BROWSER_ITEM_NAME_KEY] = name;
    json items;
    map<string, pair<jlong, string>>::iterator iter;
    for(iter = mp.begin(); iter != mp.end(); iter++) {
        json item;
        item[BROWSER_ITEM_POINTER_KEY] = iter->second.first;
        item[BROWSER_ITEM_NAME_KEY] = iter->second.second;
        item[BROWSER_ITEM_TYPE_KEY] = key;
        items[iter->first] = item;
    }
    j[BROWSER_ITEM_CHILDREN_KEY] = items;
    return j;
}

static void create_browser_item_and_add(json &parent, std::string name, int key,  map<string, pair<jlong, string>> mp) {
    parent[name] = create_browser_item(name, key, mp);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_space_celestia_mobilecelestia_core_CelestiaUniverse_c_1getChildrenForStar(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jlong pointer) {
    Universe *u = (Universe *)env->GetLongField(thiz, cunPtrFieldID);
    SolarSystem *ss = u->getSolarSystem((Star *)pointer);

    PlanetarySystem* sys = NULL;
    if (ss) sys = ss->getPlanets();

    json j;

    if (sys) {
        int sysSize = sys->getSystemSize();
        map<string, pair<jlong, string>> topLevel;
        map<string, pair<jlong, string>> planets;
        map<string, pair<jlong, string>> dwarfPlanets;
        map<string, pair<jlong, string>> minorMoons;
        map<string, pair<jlong, string>> asteroids;
        map<string, pair<jlong, string>> comets;
        map<string, pair<jlong, string>> spacecrafts;

        for (int i = 0; i < sysSize; i++) {
            Body* body = sys->getBody(i);
            if (body->getName().empty())
                continue;

            string name = body->getName(true).c_str();
            auto jitem = make_pair((jlong)body, name);

            int bodyClass  = body->getClassification();

            switch (bodyClass)
            {
                case Body::Invisible:
                    continue;
                case Body::Planet:
                    planets[name] = jitem;
                    break;
                case Body::DwarfPlanet:
                    dwarfPlanets[name] = jitem;
                    break;
                case Body::Moon:
                case Body::MinorMoon:
                    if (body->getRadius() < 100.0f || Body::MinorMoon == bodyClass)
                        minorMoons[name] = jitem;
                    else
                        topLevel[name] = jitem;
                    break;
                case Body::Asteroid:
                    asteroids[name] = jitem;
                    break;
                case Body::Comet:
                    comets[name] = jitem;
                    break;
                case Body::Spacecraft:
                    spacecrafts[name] = jitem;
                    break;
                default:
                    topLevel[name] = jitem;
                    break;
            }
        }

        map<string, pair<jlong, string>>::iterator iter;
        for(iter = topLevel.begin(); iter != topLevel.end(); iter++) {
            json item;
            item[BROWSER_ITEM_POINTER_KEY] = iter->second.first;
            item[BROWSER_ITEM_NAME_KEY] = iter->second.second;
            item[BROWSER_ITEM_TYPE_KEY] = BROWSER_ITEM_TYPE_BODY;
            j[iter->first] = item;
        }

        if (!planets.empty())
            create_browser_item_and_add(j, _("Planets"), BROWSER_ITEM_TYPE_BODY, planets);

        if (!dwarfPlanets.empty())
            create_browser_item_and_add(j, _("Dwarf Planets"), BROWSER_ITEM_TYPE_BODY, dwarfPlanets);

        if (!minorMoons.empty())
            create_browser_item_and_add(j, _("Minor Moons"), BROWSER_ITEM_TYPE_BODY, minorMoons);

        if (!asteroids.empty())
            create_browser_item_and_add(j, _("Asteroids"), BROWSER_ITEM_TYPE_BODY, asteroids);

        if (!comets.empty())
            create_browser_item_and_add(j, _("Comets"), BROWSER_ITEM_TYPE_BODY, comets);

        if (!spacecrafts.empty())
            create_browser_item_and_add(j, _("Spacecrafts"), BROWSER_ITEM_TYPE_BODY, spacecrafts);
    }

    return env->NewStringUTF(j.dump().c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_space_celestia_mobilecelestia_core_CelestiaUniverse_c_1getChildrenForBody(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jlong pointer) {
    Universe *u = (Universe *)env->GetLongField(thiz, cunPtrFieldID);

    Body *b = (Body *)pointer;
    PlanetarySystem* sys = b->getSatellites();

    json j;

    if (sys)
    {
        int sysSize = sys->getSystemSize();

        map<string, pair<jlong, string>> topLevel;
        map<string, pair<jlong, string>> minorMoons;
        map<string, pair<jlong, string>> comets;
        map<string, pair<jlong, string>> spacecrafts;

        int i;
        for (i = 0; i < sysSize; i++)
        {
            Body* body = sys->getBody(i);
            if (body->getName().empty())
                continue;

            string name = body->getName(true).c_str();
            auto jitem = make_pair((jlong)body, name);

            int bodyClass  = body->getClassification();

            if (bodyClass==Body::Asteroid) bodyClass = Body::Moon;

            switch (bodyClass)
            {
                case Body::Invisible:
                    continue;
                case Body::Moon:
                case Body::MinorMoon:
                    if (body->getRadius() < 100.0f || Body::MinorMoon == bodyClass)
                        minorMoons[name] = jitem;
                    else
                        topLevel[name] = jitem;
                    break;
                case Body::Comet:
                    comets[name] = jitem;
                    break;
                case Body::Spacecraft:
                    spacecrafts[name] = jitem;
                    break;
                default:
                    topLevel[name] = jitem;
                    break;
            }
        }

        map<string, pair<jlong, string>>::iterator iter;
        for(iter = topLevel.begin(); iter != topLevel.end(); iter++) {
            json item;
            item[BROWSER_ITEM_POINTER_KEY] = iter->second.first;
            item[BROWSER_ITEM_NAME_KEY] = iter->second.second;
            item[BROWSER_ITEM_TYPE_KEY] = BROWSER_ITEM_TYPE_BODY;
            j[iter->first] = item;
        }

        if (!minorMoons.empty())
            create_browser_item_and_add(j, _("Minor Moons"), BROWSER_ITEM_TYPE_BODY, minorMoons);

        if (!comets.empty())
            create_browser_item_and_add(j, _("Comets"), BROWSER_ITEM_TYPE_BODY, comets);

        if (!spacecrafts.empty())
            create_browser_item_and_add(j, _("Spacecrafts"), BROWSER_ITEM_TYPE_BODY, spacecrafts);
    }

    vector<Location *>* locations = b->getLocations();
    if (locations != nullptr)
    {
        map<string, pair<jlong, string>> locationsMap;

        vector<Location *>::iterator iter;
        for (iter = locations->begin(); iter != locations->end(); iter++)
        {
            string name = (*iter)->getName(true).c_str();
            locationsMap[name] = make_pair((jlong)*iter, name);
        }
        if (!locationsMap.empty())
            create_browser_item_and_add(j, _("Spacecrafts"), BROWSER_ITEM_TYPE_LOCATION, locationsMap);
    }

    return env->NewStringUTF(j.dump().c_str());
}