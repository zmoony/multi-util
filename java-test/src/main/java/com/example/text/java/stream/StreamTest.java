package com.example.text.java.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Base64Utils;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * StreamTest
 *
 * @author yuez
 * @since 2022/8/18
 */
public class StreamTest {
    @Test
    public void mapTest() {
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(new HashMap<String, Object>() {{
            put("11", 22);
        }});
        rows.add(new HashMap<String, Object>() {{
            put("22", 22);
        }});
        List<Object> sss = rows.stream().map(m -> m.put("11", 1)).collect(Collectors.toList());
        System.out.println(sss);
    }

    @Test
    public void nullStreamTest() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> ape_id = result.stream().map(map -> map.get("ape_id").toString()).collect(Collectors.toList());
        System.out.println(ape_id);
    }

    @Test
    public void pageLimitTest() {
        int page = 4;
        int limit = 2;
        int from = (page - 1) * limit;
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> collect = list.stream().skip(from).limit(limit).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void nullTest() {
        List<Integer> list = Arrays.asList(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> collect = list.stream().collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void groupByTest() {
        List<A> list = Arrays.asList(
                new A("lili", "13", "aa"),
                new A("lili", "13", "cc"),
                new A("lili", "13", "bb"),
                new A("lili", "14", "bb"),
                new A("fafa", "13", "aa"),
                new A("wuwu", "14", "bb")
        );

        Map<String, List<A>> map_singleFiled = list.stream().collect(Collectors.groupingBy(e -> {
            return e.name;
        }));
        Map<String, List<A>> map_mergeFiled = list.stream().collect(Collectors.groupingBy(e -> {
            return e.name+"_"+e.age;
        }));
        Map<String, Map<String, List<A>>> map_mergeFiled2 = list.stream()
                .collect(Collectors.groupingBy(A::getName, Collectors.groupingBy(A::getAge)));
        Map<String, Set<String>> collect = list.stream()
                .collect(Collectors.groupingBy(A::getName, Collectors.mapping(A::getAge, Collectors.toSet())));


        System.out.println(map_singleFiled);
        System.out.println(map_mergeFiled);
        System.out.println(map_mergeFiled2);
        System.out.println(collect);
    }

    @Test
    public void IntStreamTest(){
        /**
         * 12345 包含的所有整数
         * 使用 boxed()：将 Stream 中的基本类型元素装箱成对应的包装类型
         * 使用 parallel()：返回一个并行的 Stream。
         */
        IntStream.range(1,6).forEach(System.out::print);
        System.out.println("-----");
        /**
         * 123456 包括端点
         */
        IntStream.rangeClosed(1,6).forEach(System.out::print);
        System.out.println("-----");
        /**
         * 2 4 8 16 32
         * 应用于前一个元素以产生新元素的函数
         */
        Stream.iterate(2,n->n*2).limit(5).forEach(System.out::print);
        System.out.println("-----");
        /**
         * 根据指定的 Supplier 生成一个无限长度的 Stream
         */
        Stream.generate(()->new Random().nextInt(100)).limit(5).forEach(System.out::print);
        /**
         * 目前版本不适用
         * takeWhile。返回满足指定条件的元素，直到遇到第一个不满足条件的元素。
         * 使用 dropWhile()：返回不满足指定条件的元素，直到遇到第一个满足条件的元素。
         */
        /*Stream.of("apple", "banana", "orange", "pear")
                .takeWhile(s -> s.startsWith("a"))
                .forEach(System.out::print);*/

       /* Stream.of("apple", "banana", "orange", "pear")
                .dropWhile(s -> s.startsWith("a"))
                .forEach(System.out::println);*/
        /**
         * 使用 peek()：对每个元素执行指定的操作，但并不消费元素
         */
        long count = Stream.of("apple", "banana", "orange", "pear")
                .peek(System.out::print)
                .count();
        System.out.println(count);
        System.out.println("-----");


    }


    @Test
    public void jsonTest(){
            try {

                Map<String, Object> map = new HashMap<>(10);
                ArrayList<Object> arguments = new ArrayList<>(10);
                Map<String, Object> mapSub = new HashMap<>(10);
                Map<String, Object> perceivingSubjects = new HashMap<>(10);
                Map<String, Object> MOTOR_VEHICLE = new HashMap<>(10);
                Map<String, Object> contentBase64 = new HashMap<>(10);

                map.put("arguments", arguments);
                arguments.add(mapSub);
                mapSub.put("perceivingSubjects", perceivingSubjects);
                perceivingSubjects.put("MOTOR_VEHICLE", MOTOR_VEHICLE);

                mapSub.put("sceneImage", contentBase64);
                contentBase64.put("contentBase64", "wwwwwwwwwwwwwwwwwwwwwwwwwww");

                ObjectMapper mapper = new ObjectMapper();
                String s1 = mapper.writeValueAsString(map);

                System.out.println(s1);

                String json = "{\n" +
                        "    \"arguments\": [\n" +
                        "        {\n" +
                        "            \"status\": 200,\n" +
                        "            \"ret\": {\n" +
                        "                \"subjects\": [\n" +
                        "                    {\n" +
                        "                        \"sceneImage\": {\n" +
                        "                            \"uri\": \"kv://kv-image-flow_scene/4d2765b734a200004000015535f3_1677751998-3abf2280-ed17-4b60-8374-ea8946c03f88\"\n" +
                        "                        },\n" +
                        "                        \"parts\": {\n" +
                        "                            \"MOTOR_VEHICLE\": {\n" +
                        "                                \"hasFeature\": true,\n" +
                        "                                \"model\": \"2001040\",\n" +
                        "                                \"feature\": {\n" +
                        "                                    \"content\": \"\",\n" +
                        "                                    \"contentBase64\": \"AgABAQIAAgECAAMADwAEDwAAAAAPAAUPAAAAAA8ABw8AAAAACAAIAAAAAAgACQAAAAAIAAoAHoiQCAAMAAAAAAQADQAAAAAAAAAACwAOAAAAAzFfMQ8ADwsAAAABAAABAFe1tPPgRoHt7IOrjG5N8LV2/UnBvFaO6pjyLl2t1EZrVYbfOEF3u9e+L4BRh8+J24ziAFivjLAPaMwAcU43YAElQ+hjp5jMI/HS12qg7q7u8KLaSrgwwaIXy0jyQ00whhCZ44p25bbAfQOzZkZkQb+7BDuy6M6vd1EqQXsj/8YLcRn5cppyHRFenMC+y2QB0JOeYsvco7dvGx/W4v91XbYFd+yKIESHzg/uRz1AkAmPP/J+JDxPzLo0OiVDzuCBj4j0wOvFm0C0/tmK4wcd+B3a0D3uVHWWUJSqPGDWDO4AHE2PqHVgFd6Rt6Mx9uxeLZUHG0r9sBe7FjX4FOCuJBcA\"\n" +
                        "                                },\n" +
                        "                                \"crop\": {\n" +
                        "                                    \"rect\": {\n" +
                        "                                        \"x\": 1388,\n" +
                        "                                        \"y\": 315,\n" +
                        "                                        \"w\": 1147,\n" +
                        "                                        \"h\": 1501\n" +
                        "                                    },\n" +
                        "                                    \"image\": {\n" +
                        "                                        \"uri\": \"kv://kv-image-flow_motor/4d2765b734a300004000045536ac_1677751998-3abf2280-ed17-4b60-8374-ea8946c03f88\",\n" +
                        "                                        \"imageMD5\": \"9db21079e513c84e813f363f54a06a6b\"\n" +
                        "                                    }\n" +
                        "                                },\n" +
                        "                                \"attrs\": {\n" +
                        "                                    \"motorVehicleCategory\": \"VAN\",\n" +
                        "                                    \"motorVehicleColor\": \"GRAY\",\n" +
                        "                                    \"motorVehicleDirection\": \"FRONT\",\n" +
                        "                                    \"motorVehicleBrandName\": \"0249\",\n" +
                        "                                    \"motorVehicleSubbrand\": \"02490075\",\n" +
                        "                                    \"motorVehicleYearBrand\": \"024900750001\",\n" +
                        "                                    \"motorVehiclePlateType\": \"BLUE\",\n" +
                        "                                    \"motorVehiclePlateColor\": \"BLUE\",\n" +
                        "                                    \"motorVehicleLicensePlate\": \"苏ATW986\",\n" +
                        "                                    \"motorVehicleHasPlate\": \"true\",\n" +
                        "                                    \"motorVehicleDriverWearsSafeBelt\": \"true\",\n" +
                        "                                    \"motorVehicleHasPendant\": \"false\",\n" +
                        "                                    \"motorVehicleCopilotWearsSafeBelt\": \"NO_PERSON_IN_SEAT\",\n" +
                        "                                    \"motorVehicleDriverLowersSunVisor\": \"false\",\n" +
                        "                                    \"motorVehicleCopilotLowersSunVisor\": \"false\",\n" +
                        "                                    \"motorVehicleDriverIsCalling\": \"false\",\n" +
                        "                                    \"motorVehicleLowQuality\": \"false\",\n" +
                        "                                    \"motorVehiclePlateScore\": 94.75070059299469,\n" +
                        "                                    \"motorVehicleIsProvisionalPlate\": \"false\",\n" +
                        "                                    \"motorVehicleSpecialCategory\": \"UNKNOWN\"\n" +
                        "                                }\n" +
                        "                            }\n" +
                        "                        },\n" +
                        "                        \"isNotSamePerson\": false,\n" +
                        "                        \"isPedLowQuality\": false\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
                Map<String, Object> toMap = mapper.readValue(json, Map.class);
                Map<String, Object> attrs = Optional.ofNullable((List<Map<String, Object>>) toMap.get("arguments"))
                        .map(list2 -> list2.get(0))
                        .map(map1 -> (Map<String, Object>) map1.get("ret"))
                        .map(map2 -> (List<Map<String, Object>>) map2.get("subjects"))
                        .map(list1 -> list1.get(0))
                        .map(map3 -> (Map<String, Object>) map3.get("parts"))
                        .map(map3 -> (Map<String, Object>) map3.get("MOTOR_VEHICLE"))
                        .map(map4 -> (Map<String, Object>) map4.get("attrs"))
                        .orElse(Collections.emptyMap());
                System.out.println(attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }





}

@NoArgsConstructor
@AllArgsConstructor
@Getter
class A {
    String name;
    String age;
    String address;

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}