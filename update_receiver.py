import re

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    content = f.read()

# 1. Add field variable
content = re.sub(
    r'(private PodcastAdapter mPodcastAdapter;\s+protected RecyclerView mPodcastRecyclerView;\s+Context context;)',
    r'\1\n    private PodcastIntentServiceReceiver podcastIntentServiceReceiver;',
    content
)

# 2. Update onCreate variable
content = re.sub(
    r'PodcastIntentServiceReceiver podcastIntentServiceReceiver = new PodcastIntentServiceReceiver\(\);',
    r'podcastIntentServiceReceiver = new PodcastIntentServiceReceiver();',
    content
)

# 3. Update onDestroy
content = re.sub(
    r'//unregisterReceiver\(podcastIntentServiceReceiver\);',
    r'if (podcastIntentServiceReceiver != null) {\n            unregisterReceiver(podcastIntentServiceReceiver);\n        }',
    content
)

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.write(content)
