package com.dfdyz.lcaddon.client.poses;

import net.minecraft.world.entity.player.Player;
import org.ywzj.midi.pose.PoseManager;

import java.util.List;
import java.util.stream.Collectors;

public class LongPianoPoseHandler {
    public static void handle(Player player, List<Integer> posePlayNotes) {
        posePlayNotes = posePlayNotes.stream().map(i -> i -= 21).collect(Collectors.toList());
        double average = posePlayNotes.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);


        int max,min,len;
        max = posePlayNotes.stream().max(Integer::compareTo).orElse(44);
        min = posePlayNotes.stream().min(Integer::compareTo).orElse(44);
        len = max - min;

        if(len > 14){ // 大于九度，需要分手弹

        }
        else {
            if(posePlayNotes.size() <= 5){ // 音少一只手搞定

            }
            else {

            }
        }

        if (posePlayNotes.size() <= 5) {
            if (average < 44) {
                PoseManager.publish(player.getUUID(), calHandPose(average, null));
            } else {
                PoseManager.publish(player.getUUID(), calHandPose(null, average));
            }
            return;
        }

        List<Integer> leftHandNotes = posePlayNotes.stream()
                .filter(number -> number <= average)
                .collect(Collectors.toList());
        List<Integer> rightHandNotes = posePlayNotes.stream()
                .filter(number -> number > average)
                .collect(Collectors.toList());
        double posLeftHand = leftHandNotes.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        double posRightHand = rightHandNotes.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        PoseManager.publish(player.getUUID(), calHandPose(posLeftHand, posRightHand));
    }

    private static PoseManager.PlayPose calHandPose(Double posLeftHand, Double posRightHand) {
        PoseManager.PlayPose pose = new PoseManager.PlayPose();
        if (posLeftHand != null) {
            pose.leftArmRotZ = (float) (-1.5 * (33 - posLeftHand) / 33);
        }
        if (posRightHand != null) {
            pose.rightArmRotZ = (float) (1.5 * (posRightHand - 55) / 33);
        }
        pose.leftArmZ = -2.2f;
        pose.leftArmY = 3.5f;
        pose.leftArmRotX = -1.1f;
        pose.rightArmZ = -2.2f;
        pose.rightArmY = 3.5f;
        pose.rightArmRotX = -1.1f;
        return pose;
    }
}
