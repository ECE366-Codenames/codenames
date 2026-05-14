3.14  Maintain the integrity of data, being sensitive to outdated or flawed occurrences.

When we designed the PostgreSQL database, we tried to make sure the data stayed consistent. We used primary keys and foreign keys between the players and games tables so 
everything stayed connected properly. This helped avoid problems like missing or broken records when games ended or players left.

3.10 Ensure adequate testing, debugging, and review of software and related documents on which they work.

Before putting the app on the cloud, we did a lot of local testing. We checked both the database queries and the game logic, especially how the game state updates between players. 
There were some bugs with synchronization at first, but we fixed them before deployment so users wouldn’t run into those issues.

8.01 Further their knowledge of developments in the analysis, specification, design, development, maintenance and testing of software and related documents, 
together with the management of the development process.

This project pushed us to learn new things outside what we already knew. For example, we had to figure out how to use Docker and deploy on Azure, which we hadn’t really done before. 
It took some time, but it helped us understand more about real-world software development and deployment.

3.12 Work to develop software and related documents that respect the privacy of those who will be affected by that software.

We were also careful about user data. In our SQL tables, we only stored the minimum information needed for the game to work. We didn’t include unnecessary personal data, since 
there’s no reason for a game like Codenames to collect that.

7.02 Assist colleagues in professional development. Throughout the development cycle, team members actively supported one another when facing technical blockers.

As a team, we helped each other a lot. Whenever someone got stuck, like with Docker or SQL issues, we worked together to solve it. Sharing what we knew made it easier for everyone to 
improve and keep the project moving.

3.01  Strive for high quality, acceptable cost and a reasonable schedule, ensuring significant tradeoffs are clear to and accepted by the employer and the client, 
and are available for consideration by the user and the public.

We had to balance quality with time since this was for a class project. We tried to stay on schedule without rushing too much. Also, when setting up Azure, we made sure to keep it within
the free or low-cost limits so we didn’t waste resources.

6.08 Take responsibility for detecting, correcting, and reporting errors in software and associated documents on which they work.

Whenever we found bugs, we didn’t just ignore them. We tried to fix them right away, especially if they affected gameplay. Some edge cases were tricky, but we still made an effort to 
handle them instead of leaving them broken.

1.03 Approve software only if they have a well-founded belief that it is safe, meets specifications, passes appropriate tests, and does not diminish quality of life, 
diminish privacy or harm the environment.

We didn’t consider the project finished until we felt it actually worked as expected. We tested the multiplayer features and database interactions carefully, and only finalized 
it once everything was stable on Azure.

3.04 Ensure that they are qualified for any project on which they work or propose to work by an appropriate combination of education and training, and experience.

We knew this project required knowledge in different areas like databases, networking, and backend systems. Before building everything, we made sure we understood the basics so 
we wouldn’t run into major issues later.

7.04 Review the work of others in an objective, candid, and properly-documented way. When integrating the game logic with our PostgreSQL backend, we actively reviewed each other's code.
We also reviewed each other’s work during development. When someone wrote queries or set up containers, others would check it and give feedback.
This helped catch mistakes early and made the final version better overall.
