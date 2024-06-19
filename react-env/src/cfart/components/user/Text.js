import React, {useState, useEffect} from "react";
import ContentEditable from 'react-contenteditable'
import {useNode} from "@craftjs/core";
import {Slider, FormControl, FormLabel} from "@material-ui/core";

export const Text = ({text, fontSize, textAlign}) => {
    const { connectors: {connect, drag}, isActive, hasSelectedNode, hasDraggedNode, actions: {setProp} } = useNode((state) => ({
        hasSelectedNode: state.events.selected,
        hasDraggedNode: state.events.dragged,
        isActive: state.events.selected
    }));
    const [editable, setEditable] = useState(false);

    useEffect(() => {
        !hasSelectedNode && setEditable(false)
    }, [hasSelectedNode]);
    return (
        <div
            ref={ref => connect(drag(ref))}
        >
            <ContentEditable
                disabled={!editable}
                html={text}
                onChange={e =>
                    setProp(props =>
                        props.text = e.target.value.replace(/<\/?[^>]+(>|$)/g, "")
                    )
                }
                tagName="p"
                style={{fontSize: `${fontSize}px`, textAlign}}
            />
            {
                hasSelectedNode && (
                    <FormControl className="text-additional-settings" size="small">
                        <FormLabel component="legend">Font size</FormLabel>
                        <Slider
                            defaultValue={fontSize}
                            step={1}
                            min={7}
                            max={50}
                            valueLabelDisplay="auto"
                            onChange={(_, value) => {
                                setProp(props => props.fontSize = value);
                            }}
                        />
                    </FormControl>
                )
            }
        </div>
    )
}
const TextSettings = () => {
    const { actions: {setProp}, fontSize } = useNode((node) => ({
        fontSize: node.data.props.fontSize
    }));

    return (
        <>
            <FormControl size="small" component="fieldset">
                <FormLabel component="legend">Font size</FormLabel>
                <Slider
                    value={fontSize || 7}
                    step={7}
                    min={1}
                    max={50}
                    onChange={(_, value) => {
                        setProp(props => props.fontSize = value);
                    }}
                />
            </FormControl>
        </>
    )
}

Text.craft = {
    props: {
        text: "Hi",
        fontSize: 20
    },
    related: {
        settings: TextSettings
    },
    rules: {
        canDrag: (node) => node.data.props.text !== "Drag"
    }
}
