with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    content = f.read()

import re

# Find the for loop and the block to move
pattern = r'(for \(int i = 0; i < nl\.getLength\(\); i\+\+\) \{[\s\S]*?podcastList\.add\(podcast\);\s*)([\s\S]*?)(if \(mPodcastAdapter == null\) \{[\s\S]*?mPodcastAdapter\.notifyDataSetChanged\(\);\s*\})([\s\S]*?\})'

def replace_fn(match):
    part1 = match.group(1) # The for loop content up to podcastList.add
    part2 = match.group(2) # any whitespace/newlines before the if block
    part3 = match.group(3) # the if block
    part4 = match.group(4) # the closing brace of the for loop

    # We want to move part3 outside the for loop, so after part4.
    # The for loop ends at part4 (which should just be "}")

    return part1 + part2 + part4 + "\n                    " + part3

new_content = re.sub(pattern, replace_fn, content)

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.write(new_content)
