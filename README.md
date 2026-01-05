# Store Priority Queue (DLL with Dummy Node) – Java

# Overview
This project implements a simple priority queue of Store objects using a doubly linked list with a dummy node.  

The priority rule is:
Highest sales = highest priority

# How It Works
# Data Structure
'PriorityQueueDLLDummy' uses a circular doubly linked list with a dummy node:
- The dummy node simplifies edge cases (empty list, insert/remove at ends).
- New items are inserted right after the dummy node.
- To remove the highest priority, the code scans the list to find the max sales node.

# Time Complexity
- add(Store a): O(1)
- getHighestSalesStore(): O(n) 
- clear(): O(n)

# Input File Format
This program expects pairs of lines:
1. Owner name
2. Sales number

