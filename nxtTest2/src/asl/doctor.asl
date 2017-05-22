// Agent doctor in project asd

/* Initial beliefs and rules */

urgent([]).
serious([]).
minor([]).

/* Initial goals */

/* Plans */

+victimLocated(Victim) : true <-
	+victims([]);
	!putIntoBucket(Victim);
	!concatenateBuckets;
	!printVictims;
	-victims([]).
	
+!printBuckets : urgent(A) & serious(B) & minor(C) <-
	.print("Urgent: ", A, " Serious: ", B, " Minor: ", C).
	
+!printVictims : victims(A) <-
	.print("Victims: ", A);
	!printBuckets.
	
+!putIntoBucket([X, Y, Z]) : Z == 0 & urgent(List) & not .member([X, Y, Z], List) <-
	.concat(List, [[X, Y, Z]], NewList);
	-urgent(List);
	+urgent(NewList).
	
+!putIntoBucket([X, Y, Z]) : Z == 2 & serious(List) & not .member([X, Y, Z], List) <-
	.concat(List, [[X, Y, Z]], NewList);
	-serious(List);
	+serious(NewList).
	
+!putIntoBucket([X, Y, Z]) : Z == 1 & minor(List) & not .member([X, Y, Z], List) <-
	.concat(List, [[X, Y, Z]], NewList);
	-minor(List);
	+minor(NewList).
	
+!concatenateBuckets : victims(A) & urgent(B) & serious(C) & minor(D) <-
	!concatenateUrgent(A, B, C, D).
	
+!concatenateUrgent(A, B, C, D) : victims(A) & not .empty(B) <-
	.concat(A, [B], X);
	+victims(X);
	!concatenateSerious(X, B, C, D).

+!concatenateUrgent(X, B, C, D) <- !concatenateSerious(X, B, C, D).

+!concatenateSerious(X, B, C, D) : victims(A) & not .empty(C) <-
	.concat(X, [C], Y);
	+victims(Y);
	!concatenateMinor(Y, B, C, D).	
	
+!concatenateSerious(Y, B, C, D) <- !concatenateMinor(Y, B, C, D).
	
+!concatenateMinor(Y, B, C, D) : victims(A) & not .empty(D) <-
	.concat(Y, [D], Z);
	+victims(Z).
	
+!concatenateMinor(Y, B, C, D).
