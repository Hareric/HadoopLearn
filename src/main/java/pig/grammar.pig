
-- 读取文件 指定分隔符 并为每一个列指定列名
A = load 'src/main/resources/pigData/dialy_show_guests' using PigStorage(',') 
	as (year:int, Occupation:chararray, date:chararray, OccupationLarger:chararray, name:chararray);
/*
-- 输出至指定路径
STORE B INTO 'src/main/resources/pigData/dialy_show_guests_2' USING PigStorage();


-- 通过列名 进行分组和组合 B的结构是:{group:(year,Occupation,date),A:bag:{tuple,tuple}}
B = GROUP A BY (year,Occupation,date);

-- C的结构是C:{group:(year,Occupation,date),double} 
-- 此处求平均值 并不是对A.year所有值求平均值，而是将(year,Occupation,date)作为key，以及他们对应的values的A.year求平均值
C = FOREACH B GENERATE group, AVG(A.year);


-- Count
E = GROUP A ALL;
F = FOREACH E GENERATE COUNT(A);
DUMP F;


-- FLATTEN用于对元组（tuple）和包（bag）“解嵌套”（un-nest）： {year, Occupation, date, double}
G = GROUP A BY (year,Occupation,date);
H = FOREACH G GENERATE FLATTEN(group), AVG(A.year);
DUMP H;
*/

-- Tuple
I = LOAD 'src/main/resources/pigData/dialy_show_guests' using PigStorage(',') 
	 AS 
	(T : tuple (year:int, Occupation:chararray, 
	date:chararray, OccupationLarger:chararray, name:chararray));
DUMP I -- 输出为() () () () ....
 
