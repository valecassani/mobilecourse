package it.polimi.mobilecourse.expenses;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Matteo on 21/12/2014.
 */
public class Entity {



        public Entity() {
            elements = new HashMap<String,String>();
        }
        private Map<String,String> elements;
        private void addElement(String key, String value) {
            elements.put(key, value);
        }
        public String get(String name) {
            return elements.get(name);
        }
        public String getFist() {
            return elements.get(elements.keySet().toArray()[0]);
        }
        static Entity jsonObjectToEntity(JSONObject jsonObject) {
            Entity result = new Entity();
            try {
                JSONArray names = jsonObject.names();
                JSONArray values = jsonObject.toJSONArray(names);
                for(int i = 0 ; i < values.length(); i++){
                    result.addElement(names.getString(i), jsonObject.getString(names.getString(i)));
                }
            }
            catch(Exception e) {
                return null;
            }
            return result;
        }
        static ArrayList<Entity> jsonArrayToEntityList(JSONArray jsonArray) {
            if (jsonArray == null) return null;
            ArrayList<Entity> result = new ArrayList<Entity>();
            for(int i=0; i<jsonArray.length(); i++) {
                try {
                    result.add(jsonObjectToEntity((JSONObject) jsonArray.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        static ArrayList<String> entityListToStringList(ArrayList<Entity> list) {
            ArrayList<String> result = new ArrayList<String>();
            for(Entity element : list) {
                result.add(element.getFist());
            }
            return result;
        }
        public ArrayList<String> values() {
            return new ArrayList<String>(elements.values());
        }
}
