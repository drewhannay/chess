package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.drewhannay.chesscrafter.rules.endconditions.EndCondition;
import com.drewhannay.chesscrafter.rules.legaldestinationcropper.LegalDestinationCropper;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.drewhannay.chesscrafter.rules.promotionmethods.PromotionMethod;
import com.drewhannay.chesscrafter.timer.ChessTimer;
import com.google.common.collect.Maps;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public final class GsonUtility {
    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EndCondition.class, new InterfaceAdapter<EndCondition>());
        builder.registerTypeAdapter(TurnKeeper.class, new InterfaceAdapter<TurnKeeper>());
        builder.registerTypeAdapter(LegalDestinationCropper.class, new InterfaceAdapter<LegalDestinationCropper>());
        builder.registerTypeAdapter(PostMoveAction.class, new InterfaceAdapter<PostMoveAction>());
        builder.registerTypeAdapter(PromotionMethod.class, new InterfaceAdapter<PromotionMethod>());
        builder.registerTypeAdapter(ChessTimer.class, new InterfaceAdapter<ChessTimer>());
        builder.registerTypeAdapter(Map.class, new MapAdapter());

        return builder.create();
    }

    public static <T> Object fromJson(String jsonString, Class<T> clazz) {
        return getGson().fromJson(jsonString, clazz);
    }

    private static class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
        @Override
        public final JsonElement serialize(final T object, final Type interfaceType, final JsonSerializationContext context) {
            final JsonObject member = new JsonObject();

            member.addProperty("type", object.getClass().getName());
            member.add("data", context.serialize(object));

            return member;
        }

        @Override
        public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject member = (JsonObject) elem;
            final JsonElement typeString = get(member, "type");
            final JsonElement data = get(member, "data");
            final Type actualType = typeForName(typeString);

            return context.deserialize(data, actualType);
        }

        private Type typeForName(final JsonElement typeElem) {
            try {
                return Class.forName(typeElem.getAsString());
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        private JsonElement get(final JsonObject wrapper, final String memberName) {
            final JsonElement elem = wrapper.get(memberName);

            if (elem == null)
                throw new JsonParseException("no '" + memberName + "' member found in json file.");

            return elem;
        }
    }

    private static class MapAdapter implements JsonSerializer<Map<?, ?>>, JsonDeserializer<Map<?, ?>> {
        @Override
        public JsonElement serialize(Map<?, ?> m, Type typeOfT, JsonSerializationContext context) {
            JsonArray rv = new JsonArray();
            for (Object k : m.keySet()) {
                JsonObject kv = new JsonObject();
                kv.add("k", context.serialize(k));
                kv.addProperty("ktype", k.getClass().getName());
                kv.add("v", context.serialize(m.get(k)));
                kv.addProperty("vtype", m.get(k).getClass().getName());
                rv.add(kv);
            }
            return rv;
        }

        @Override
        public Map<?, ?> deserialize(JsonElement _json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray json = (JsonArray) _json;
            Map<Object, Object> rv = Maps.newHashMap();
            for (int i = 0; i < json.size(); i++) {
                JsonObject o = (JsonObject) json.get(i);
                String ktype = o.getAsJsonPrimitive("ktype").getAsString();
                String vtype = o.getAsJsonPrimitive("vtype").getAsString();
                Class<?> kklass = null;
                Class<?> vklass = null;
                try {
                    kklass = Class.forName(ktype);
                    vklass = Class.forName(vtype);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new JsonParseException(e.getMessage());
                }
                Object k = context.deserialize(o.get("k"), kklass);
                Object v = context.deserialize(o.get("v"), vklass);
                rv.put(k, v);
            }
            return rv;
        }
    }

}
