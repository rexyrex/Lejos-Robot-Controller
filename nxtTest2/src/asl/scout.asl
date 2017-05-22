// Agent scout in project asd

/* Initial beliefs and rules */

/* Initial goals */
!startSearch.
!explore.
//!checkVictims.

/* Plans */

+!startSearch : true <-
robot.startSearch(1);
.wait(3000);
!explore.

+!startSearch.


+!explore : true <-
	
	robot.distance2(A, B, C);
	
	+checkIfVictim(A, B, C);
	
	.wait(1000);
	!explore.
		
+checkIfVictim(X, Y, Z) : Z == 0 | Z == 2 | Z == 1 <-
	.send(doctor, tell, victimLocated([X, Y, Z])).
	
/*+!checkVictims : true <-
	+checkIfVictim(0, 0, 0); .wait(100);
	+checkIfVictim(0, 1, purple); .wait(100);
	+checkIfVictim(0, 2, 1); .wait(100);
	+checkIfVictim(1, 1, 2); .wait(100);
	+checkIfVictim(0, 7, 0); .wait(100);
	+checkIfVictim(0, 1, 0); .wait(100);
	+checkIfVictim(0, 2, 1); .wait(100);
	+checkIfVictim(1, 1, 2); .wait(100);
	+checkIfVictim(0, 0, 0); .wait(100);
	+checkIfVictim(0, 0, orange); .wait(100);
	+checkIfVictim(0, 1, purple); .wait(100);
	+checkIfVictim(0, 2, yellow); .wait(100);
	+checkIfVictim(1, 2, 2); .wait(100);
	+checkIfVictim(1, 8, 2); .wait(100);
	+checkIfVictim(0, 8, orange); .wait(100);
	+checkIfVictim(0, 8, purple); .wait(100);
	+checkIfVictim(0, 8, yellow); .wait(100);
	+checkIfVictim(1, 6, 2); .wait(100). */