with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    lines = f.readlines()

# Clean up duplicate declaration
new_lines = []
found_decl = False
for line in lines:
    if 'private PodcastIntentServiceReceiver podcastIntentServiceReceiver;' in line:
        if not found_decl:
            new_lines.append(line)
            found_decl = True
    else:
        new_lines.append(line)

# Add unregisterReceiver to onDestroy
final_lines = []
in_ondestroy = False
for line in new_lines:
    final_lines.append(line)
    if 'public void onDestroy() {' in line:
        in_ondestroy = True
    elif in_ondestroy and 'super.onDestroy();' in line:
        final_lines.append('        if (podcastIntentServiceReceiver != null) {\n')
        final_lines.append('            unregisterReceiver(podcastIntentServiceReceiver);\n')
        final_lines.append('        }\n')
        in_ondestroy = False

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.writelines(final_lines)
