with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if '                    if (mPodcastAdapter == null) {' in line:
        new_lines.append('                        if (mPodcastAdapter == null) {\n')
    elif '                        mPodcastAdapter = new PodcastAdapter(podcastList);' in line:
        new_lines.append('                            mPodcastAdapter = new PodcastAdapter(podcastList);\n')
    elif '                        mPodcastRecyclerView.setAdapter(mPodcastAdapter);' in line:
        new_lines.append('                            mPodcastRecyclerView.setAdapter(mPodcastAdapter);\n')
    elif '                    } else {' in line:
        new_lines.append('                        } else {\n')
    elif '                        mPodcastAdapter.notifyDataSetChanged();' in line:
        new_lines.append('                            mPodcastAdapter.notifyDataSetChanged();\n')
    elif '                    }' in line and len(line.strip()) == 1:
        new_lines.append('                        }\n')
    else:
        new_lines.append(line)

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.writelines(new_lines)
