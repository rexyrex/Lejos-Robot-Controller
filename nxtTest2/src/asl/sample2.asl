/* Initial beliefs and rules */

/* Initial goals */
!waitsec.
!startSearch.


/* Plans */
+!waitsec : true <-
.print("waiting");
.wait(1000).

+!startSearch : true <-
robot.startSearch(1);
.wait(3000);
!start.

+!startSearch.

+!start : true
<- robot.distance2(A,B,C);
.wait(1000);
.print("x = ", A);
.print("y = ", B);
.print("Color = ", C);
.wait(1000);
!start.