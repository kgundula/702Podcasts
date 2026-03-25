with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if 'protected RecyclerView mPodcastRecyclerView;' in line:
        new_lines.append(line)
        new_lines.append('    private PodcastIntentServiceReceiver podcastIntentServiceReceiver;\n')
    elif 'PodcastIntentServiceReceiver podcastIntentServiceReceiver = new PodcastIntentServiceReceiver();' in line:
        new_lines.append('        podcastIntentServiceReceiver = new PodcastIntentServiceReceiver();\n')
    elif '//unregisterReceiver(podcastIntentServiceReceiver);' in line:
        new_lines.append('        if (podcastIntentServiceReceiver != null) {\n')
        new_lines.append('            unregisterReceiver(podcastIntentServiceReceiver);\n')
        new_lines.append('        }\n')
    else:
        new_lines.append(line)

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.writelines(new_lines)
