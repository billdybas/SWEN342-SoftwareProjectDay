# SWEN342-SoftwareProjectDay

# The Problem

* The firm at which all the following activities take place has a strict policy of opening at 8:00 and closing at 5:00. Employees may arrive anytime between 8:00 and 8:30, take lunch for at least 30 minutes but no more than an hour, and leave anytime from 4:30 to 5:00 as long as they work at least 8 hours. For your simulation, one minute of simulated time takes 10ms of real time (thus 8 hours takes 4800ms or 4.8 seconds).
* A software project manager is in charge of 3 teams of developers, each team consisting of a team lead and 3 additional developers (12 developers in all).
* When he arrives at 8:00 each day, the manager engages in daily planning activites and then waits (doing administrivia) until all the team leads arrive at his office. When all the leads have arrived, they knock on the manager's door and enter for their daily 15 minute standup meeting.
* After the meeting, the team leads wait for all the members of their team to arrive. When all members of a team are present, the team lead waits for the one-and-only team conference room to become available, enters the room with all team members, and holds a holds a team-based standup meeting for 15 minutes.
* At any point during the day, a team member may ask a question of his or her team lead. There is an 50% chance that the team lead can answer the question, at which time the lead and the team member return to work.
* If the team lead is the one with the question, or if the question is one of the 50% the lead cannot answer, the lead and the member asking the question go to the project manager's office to have the question answered. When let into the manager's office, the team lead asks the question, the manager provides the answer, and the team lead (and possibly the member with the question) return to work. Of course, the team may have to wait for one or more other teams to have their questions answered first. We will also assume it always takes 10 minutes to ask and answer a question.
* The manager has two daily executive meetings, each lasting one hour, one from 10:00 to 11:00 and one from 2:00 to 3:00. In addition, the manager eats lunch for one hour starting as close to from 12:00 as possible to 1:00. If in the middle of answering a question when a meeting or lunch begins, the manager finishes answering the question and then goes to the meeting or lunch. Any other teams with questions simply wait for the manager to return.
* At 4:00 every day leaders and members of all teams start assembling in the conference room for a project status update. It is expected that members will finish arriving by 4:15, allowing 15 minutes to clean up any work in progress. When all members have gathered, the manager spends 15 minutes dicussing the project status.
* Employees start leaving from 4:30 to 5:00 as they complete their 8 hour days. The project manager is the last to leave at 5:00.
* When not asking questions, at meetings, or at lunch, team leads and developers are hard at work designing, coding and testing.
* When not answering questions, at meetings, or at lunch, the manager does whatever it is managers do (looking for deals on Woot!, reading blogs, thinking of ways to make the developers' lives miserable, etc.)
* When developers ask the team lead a question that the lead can answer no time elapses (i.e., response is instantaneous).
* Developers may not ask questions during the morning standup meeting or the afternoon project status meeting.

# Running the Program

Compile each source file and run the `main` method in `Main.java`
