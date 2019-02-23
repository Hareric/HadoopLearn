A = load 'src/main/resources/pigData/dialy_show_guests' using PigStorage(',') AS (year:chararray,occupation:chararray,date:chararray,group:chararray,gusetlist:chararray);
B = foreach A generate occupation,date;
C = foreach B generate occupation,ToDate(date,'MM/dd/yy') as date;
D = filter C by ((date> ToDate('1/11/99','MM/dd/yy')) AND (date<ToDate('6/11/99','MM/dd/yy')));
E = group D by occupation;
F = foreach E generate group, COUNT(D) as cnt;
G = order F by cnt desc;
H = limit G 5;